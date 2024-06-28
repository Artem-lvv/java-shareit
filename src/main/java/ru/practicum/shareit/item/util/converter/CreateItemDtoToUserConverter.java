package ru.practicum.shareit.item.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.CreateItemDto;

@Component
public class CreateItemDtoToUserConverter implements Converter<CreateItemDto, Item> {
    @Override
    public Item convert(CreateItemDto src) {
        return Item.builder()
                .name(src.name())
                .description(src.description())
                .available(src.available())
                .build();
    }
}
