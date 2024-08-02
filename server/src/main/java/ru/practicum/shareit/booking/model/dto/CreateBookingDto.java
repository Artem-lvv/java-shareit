package ru.practicum.shareit.booking.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.util.valid.BookingStartDateBeforeEndDate;

import java.time.LocalDateTime;

@Builder
@BookingStartDateBeforeEndDate
public record CreateBookingDto(
        @Positive
        Long itemId,
        @FutureOrPresent
        LocalDateTime start,
        @FutureOrPresent
        LocalDateTime end,
        BookingStatus status
) {
}
