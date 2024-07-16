package ru.practicum.shareit.booking.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

public record CreateBookingDto (
        @Positive
        Long itemId,
        @FutureOrPresent
        LocalDateTime start,
        @FutureOrPresent
        LocalDateTime end,
        BookingStatus status
) {
}
