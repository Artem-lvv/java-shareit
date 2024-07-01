package ru.practicum.shareit.item.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;

@Component
public class UpdateItemToItemDtoConverter implements Converter<Item, ItemDto> {
    @Override
    public ItemDto convert(Item src) {
        return ItemDto.builder()
                .id(src.getId())
                .name(src.getName())
                .description(src.getDescription())
                .available(src.isAvailable())
                .build();
    }
}
