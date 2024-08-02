package ru.practicum.shareit.ItemRequest.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.ItemRequest.model.ItemRequest;
import ru.practicum.shareit.ItemRequest.model.dto.CreateItemRequestDto;

@Component
public class CreateItemRequestDtoToItemRequestConverter implements Converter<CreateItemRequestDto, ItemRequest> {
    @Override
    public ItemRequest convert(CreateItemRequestDto src) {
        return ItemRequest.builder()
                .description(src.description())
                .build();
    }
}
