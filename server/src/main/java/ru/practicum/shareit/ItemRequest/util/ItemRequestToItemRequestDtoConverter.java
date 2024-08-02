package ru.practicum.shareit.ItemRequest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.ItemRequest.model.ItemRequest;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestDto;

@Component
public class ItemRequestToItemRequestDtoConverter implements Converter<ItemRequest, ItemRequestDto> {
    @Override
    public ItemRequestDto convert(ItemRequest src) {
        return ItemRequestDto.builder()
                .id(src.getId())
                .description(src.getDescription())
                .created(src.getCreated())
                .build();
    }
}