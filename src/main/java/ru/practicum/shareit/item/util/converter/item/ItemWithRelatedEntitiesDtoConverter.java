package ru.practicum.shareit.item.util.converter.item;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.item.ItemWithRelatedEntities;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;

import java.util.Objects;

@Component
public class ItemWithRelatedEntitiesDtoConverter implements Converter<ItemWithRelatedEntities, ItemWithRelatedEntitiesDto> {
    @Override
    public ItemWithRelatedEntitiesDto convert(ItemWithRelatedEntities src) {
        return ItemWithRelatedEntitiesDto.builder()
                .id(src.getItem().getId())
                .name(src.getItem().getName())
                .description(src.getItem().getDescription())
                .available(src.getItem().isAvailable())
                .lastBooking(Objects.nonNull(src.getLastBooking()) ? BookingIdAndBookerIdDto.builder()
                        .id(src.getLastBooking().getId())
                        .bookerId(src.getLastBooking().getBooker().getId())
                        .build()
                        : null)
                .nextBooking(Objects.nonNull(src.getNextBooking()) ? BookingIdAndBookerIdDto.builder()
                        .id(src.getNextBooking().getId())
                        .bookerId(src.getNextBooking().getBooker().getId())
                        .build()
                        : null)
                .comments(Objects.nonNull(src.getComments()) ? src.getComments()
                        .stream()
                        .map(comment -> CommentDto.builder()
                                .id(comment.getId())
                                .text(comment.getText())
                                .authorName(comment.getAuthor().getName())
                                .created(comment.getCreated())
                                .build())
                        .toList()
                        : null)
                .build();
    }
}
