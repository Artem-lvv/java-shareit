package ru.pracicum.shareit.booking.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import ru.pracicum.shareit.booking.model.BookingStatus;
import ru.pracicum.shareit.booking.util.valid.BookingStartDateBeforeEndDate;


import java.time.LocalDateTime;

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
