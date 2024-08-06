package ru.practicum.shareit.item.model.item.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

import java.util.List;

@Builder
public record ItemWithRelatedEntitiesDto(
        Long id,
        String name,
        String description,
        boolean available,
        BookingIdAndBookerIdDto lastBooking,
        BookingIdAndBookerIdDto nextBooking,
        List<CommentDto> comments
){
}
