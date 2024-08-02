package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Builder
public record CreateBookingDto(
        Long itemId,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status
) {
}
