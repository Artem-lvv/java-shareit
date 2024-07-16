package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.dto.UpdateBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto createBookingDto, final Long userId);
    BookingDto updateBookingStatus(final Long bookingId, final boolean approved, final Long userId);
    BookingDto getBooking(final Long bookingId, final Long userId);
    List<BookingDto> getBookings(final String state, final Long userId);
    List<BookingDto> getBookingsForOwner(final String state,final Long userId);
}
