package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.TypeState;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto createBookingDto, final Long userId);

    BookingDto updateBookingStatus(final Long bookingId, final boolean approved, final Long userId);

    BookingDto getBooking(final Long bookingId, final Long userId);

    List<BookingDto> getBookings(final TypeState state, final Long userId);

    List<BookingDto> getBookingsForOwner(final TypeState state,final Long userId);
}
