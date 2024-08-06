package ru.practicum.shareit.itemRequest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.model.dto.CreateItemRequestDto;

@Component
public class CreateItemRequestDtoToItemRequestConverter implements Converter<CreateItemRequestDto, ItemRequest> {
    @Override
    public ItemRequest convert(CreateItemRequestDto src) {
        return ItemRequest.builder()
                .description(src.description())
                .build();
    }
}
