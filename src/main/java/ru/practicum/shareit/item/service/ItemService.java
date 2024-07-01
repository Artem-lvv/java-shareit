package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.dto.CreateItemDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.UpdateItemDto;

import java.util.List;

@Component
public interface ItemService {
    ItemDto createItem(Long itemId, CreateItemDto createItemDto);

    ItemDto updateItem(Long itemId, Long userId, UpdateItemDto updateItemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getItemsByText(String text);
}
