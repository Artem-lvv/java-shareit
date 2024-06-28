package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
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
    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public ItemDto createItem(Long itemId, CreateItemDto createItemDto) {
        Optional<User> userById = userRepository.findById(itemId);
        if (userById.isEmpty()) {
            throw new EntityNotFoundByIdException("Item", itemId.toString());
        }

        Item newItem = cs.convert(createItemDto, Item.class);

        if (Objects.isNull(newItem)) {
            throw new InternalServerException("Failed created user");
        }

        newItem.setOwner(userById.get());
        newItem = itemRepository.save(newItem);

        log.info("Create item {}", newItem);

        return cs.convert(newItem, ItemDto.class);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, UpdateItemDto updateItemDto) {
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

        Item updateItem = itemRepository.update(itemById.get());
        log.info("Update item {}", updateItem);

        return cs.convert(updateItem, ItemDto.class);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Optional<Item> itemById = itemRepository.findById(itemId);
        if (itemById.isPresent()) {
            return cs.convert(itemById.get(), ItemDto.class);
        } else {
            throw new EntityNotFoundByIdException("Item", itemId.toString());
        }
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        List<Item> allItemsByUserId = itemRepository.findAllItemsByUserId(userId);

        return allItemsByUserId
                .stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> itemsByName = itemRepository.findItemsByText(text);

        return itemsByName
                .stream()
                .map(item -> cs.convert(item, ItemDto.class))
                .toList();
    }

}
