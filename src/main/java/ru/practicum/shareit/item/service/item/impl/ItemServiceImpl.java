package ru.practicum.shareit.item.service.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.model.item.ItemWithRelatedEntities;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.item.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public ItemDto createItem(final Long userId, final CreateItemDto createItemDto) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty()) {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }

        Item newItem = cs.convert(createItemDto, Item.class);

        if (Objects.isNull(newItem)) {
            throw new InternalServerException("Failed created item");
        }

        newItem.setOwner(userById.get());
        newItem = itemRepository.save(newItem);

        log.info("Create item {}", newItem);

        return cs.convert(newItem, ItemDto.class);
    }

    @Override
    public ItemDto updateItem(final Long itemId, final Long userId, final UpdateItemDto updateItemDto) {
        Optional<Item> itemById = itemRepository.findById(itemId);
        if (itemById.isEmpty()) {
            throw new EntityNotFoundByIdException("Item", itemId.toString());
        }

        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty()) {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }

        if (!itemById.get().getOwner().equals(userById.get())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The owner of the item is different from the user in the headers");
        }

        if (Objects.nonNull(updateItemDto.name())) {
            itemById.get().setName(updateItemDto.name());
        }

        if (Objects.nonNull(updateItemDto.description())) {
            itemById.get().setDescription(updateItemDto.description());
        }

        if (Objects.nonNull(updateItemDto.available())) {
            itemById.get().setAvailable(updateItemDto.available());
        }

        Item updateItem = itemRepository.save(itemById.get());
        log.info("Update item {}", updateItem);

        return cs.convert(updateItem, ItemDto.class);
    }

    @Override
    public ItemWithRelatedEntitiesDto getItemById(final Long itemId, final Long userId) {
        Optional<ItemWithRelatedEntities> itemById = itemRepository
                .findItemById(itemId, userId);

        if (itemById.isPresent()) {
            itemById.get().setComments(commentRepository.findAllByItem_Id(itemId));
            return cs.convert(itemById.get(), ItemWithRelatedEntitiesDto.class);

        } else {
            throw new EntityNotFoundByIdException("Item", itemId.toString());
        }
    }

    @Override
    public List<ItemWithRelatedEntitiesDto> getAllItemsByOwnerId(final Long userId) {
        List<ItemWithRelatedEntities> allItemsByOwnerId = itemRepository.findAllItemsByOwnerId(userId);

        List<ItemWithRelatedEntitiesDto> resultList = null;
        try {
            resultList = allItemsByOwnerId
                    .stream()
                    .map(itemWithRelatedEntities -> cs.convert(itemWithRelatedEntities, ItemWithRelatedEntitiesDto.class))
                    .toList();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
        }

        return resultList;
    }

    @Override
    public List<ItemDto> getItemsByText(final String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        String textSearch = "%" + text + "%";
        List<Item> itemsByName = itemRepository.findAllItemsByText(textSearch);

        return itemsByName
                .stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

}
