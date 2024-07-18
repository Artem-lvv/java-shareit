package ru.practicum.shareit.item.service.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;

import java.util.List;

@Component
public interface ItemService {
    ItemDto createItem(final Long itemId, final CreateItemDto createItemDto);

    ItemDto updateItem(final Long itemId, Long userId, final UpdateItemDto updateItemDto);

    ItemWithRelatedEntitiesDto getItemById(final Long itemId, final Long userId);

    List<ItemWithRelatedEntitiesDto> getAllItemsByOwnerId(final Long userId);

    List<ItemDto> getItemsByText(final String text);
}
