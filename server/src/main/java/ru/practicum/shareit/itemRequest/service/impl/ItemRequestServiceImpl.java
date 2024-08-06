package ru.practicum.shareit.itemRequest.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.model.ItemRequestWithItems;
import ru.practicum.shareit.itemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.itemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.itemRequest.service.ItemRequestService;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public ItemRequestDto createItemRequest(final CreateItemRequestDto createItemRequestDto, final Long userId) {
        final Optional<User> requestor = userRepository.findById(userId);

        if (requestor.isEmpty()) {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }

        final ItemRequest itemRequest = cs.convert(createItemRequestDto, ItemRequest.class);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor.get());
        final ItemRequest saveItemRequest = itemRequestRepository.save(itemRequest);

        log.info("Created item request {}", saveItemRequest);

        return cs.convert(saveItemRequest, ItemRequestDto.class);
    }

    @Override
    public List<ItemRequestWithItemsDto> getRequestsByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }

        List<ItemRequest> allByRequestorId = itemRequestRepository.findAllByRequestor_Id(userId);
        return allByRequestorId
                .stream()
                .map(itemRequest -> ItemRequestWithItems
                        .builder()
                        .id(itemRequest.getId())
                        .description(itemRequest.getDescription())
                        .created(itemRequest.getCreated())
                        .items(itemRepository.findAllByRequestIdWithOwner(itemRequest.getId()))
                        .build())
                .map(itemRequestWithItems -> cs.convert(itemRequestWithItems, ItemRequestWithItemsDto.class))
                .toList();

    }

    @Override
    public ItemRequestWithItemsDto getRequestsById(final Long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);

        if (itemRequest.isEmpty()) {
            throw new EntityNotFoundByIdException("request", requestId.toString());
        }

        ItemRequestWithItems itemRequestWithItems = ItemRequestWithItems
                .builder()
                .id(itemRequest.get().getId())
                .description(itemRequest.get().getDescription())
                .created(itemRequest.get().getCreated())
                .items(itemRepository.findAllByRequestIdWithOwner(itemRequest.get().getId()))
                .build();

        return cs.convert(itemRequestWithItems, ItemRequestWithItemsDto.class);
    }

    @Override
    public List<ItemRequestDto> getAllByPage(Integer from, Integer size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from / size, size, sortById);
        List<ItemRequestDto> result = new ArrayList<>();

        do {
            Page<ItemRequest> itemRequestsPage = itemRequestRepository.findAll(page);
            itemRequestsPage.getContent().forEach(itemRequest -> {
                result.add(cs.convert(itemRequest, ItemRequestDto.class));
            });

            if (itemRequestsPage.hasNext()) {
                page = itemRequestsPage.nextPageable();
            } else {
                page = null;
            }
        } while (page != null);

        return result;
    }
}
