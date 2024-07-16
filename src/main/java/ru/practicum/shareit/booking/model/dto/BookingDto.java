package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;

@Builder
public record BookingDto(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        ItemDto item,
        UserDto booker,
        BookingStatus status
) {
}
