package ru.practicum.shareit.ItemRequest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.ItemRequest.model.ItemRequestWithItems;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.item.model.item.dto.ItemIdNameIdOwnerDto;

import java.util.Collections;
import java.util.Objects;

@Component
public class ItemRequestWithItemsDtoConverter implements Converter<ItemRequestWithItems, ItemRequestWithItemsDto> {
    @Override
    public ItemRequestWithItemsDto convert(ItemRequestWithItems src) {
        return ItemRequestWithItemsDto
                .builder()
                .id(src.getId())
                .description(src.getDescription())
                .created(src.getCreated())
                .items(Objects.nonNull(src.getItems()) ? src.getItems()
                        .stream()
                        .map(item -> ItemIdNameIdOwnerDto
                                .builder()
                                .id(item.getId())
                                .name(item.getName())
                                .ownerId(item.getOwner().getId())
                                .build()).toList()
                        : Collections.emptyList())
                .build();
    }
}
