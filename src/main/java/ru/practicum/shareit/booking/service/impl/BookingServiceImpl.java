package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public BookingDto createBooking(CreateBookingDto createBookingDto, final Long userId) {
        if ((Objects.isNull(createBookingDto.start()) || Objects.isNull(createBookingDto.end()))
                || (createBookingDto.end().isBefore(createBookingDto.start())
                || createBookingDto.start().equals(createBookingDto.end()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Booking date is null or end date is greater than start date");
        }

        User userById = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundByIdException("User", userId.toString()));

        Optional<Item> itemById = itemRepository.findById(createBookingDto.itemId());
        if (itemById.isEmpty()) {
            throw new EntityNotFoundByIdException("Item", createBookingDto.itemId().toString());
        } else if (!itemById.get().isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Item by id [%s] not found or is unavailable".formatted(createBookingDto.itemId()));
        }

        final Booking newBooking = cs.convert(createBookingDto, Booking.class);
        newBooking.setBooker(userById);
        newBooking.setItem(itemById.get());
        newBooking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(newBooking);
        log.info("Create {}", newBooking);

        return BookingDto.builder()
                .id(newBooking.getId())
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .item(cs.convert(newBooking.getItem(), ItemDto.class))
                .booker(cs.convert(newBooking.getBooker(), UserDto.class))
                .status(newBooking.getStatus())
                .build();
    }

    @Override
    public BookingDto updateBookingStatus(final Long bookingId, final boolean approved, final Long userId) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundByIdException("Booking", bookingId.toString()));

        BookingStatus newBookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        if (booking.getStatus() == newBookingStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current status is equal to the new one");
        }

        booking.setStatus(newBookingStatus);
        bookingRepository.save(booking);

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(cs.convert(booking.getItem(), ItemDto.class))
                .booker(cs.convert(booking.getBooker(), UserDto.class))
                .status(booking.getStatus())
                .build();
    }

    @Override
    public BookingDto getBooking(final Long bookingId, final Long userId) {
//        final Long bookingIdLong;
//
//        try {
//            bookingIdLong = Long.parseLong(bookingId);
//        } catch (NumberFormatException e) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "Invalid booking number [%s]".formatted(bookingId));
//        }
        Booking booking = bookingRepository.findBookingByBookingIdAndBookerIdOrItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundByIdException("Booking", bookingId.toString()));

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(cs.convert(booking.getItem(), ItemDto.class))
                .booker(cs.convert(booking.getBooker(), UserDto.class))
                .status(booking.getStatus())
                .build();
    }

    @Override
    public List<BookingDto> getBookings(final String state, final Long userId) {
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookings(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookings(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookings(userId);
                break;
            case "WAITING":
                bookings = bookingRepository.findBookingsByStatusAndBookerId(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findBookingsByStatusAndBookerId(userId, BookingStatus.REJECTED);
                break;
            case "ALL":
                bookings = bookingRepository.findAllByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "id"));
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        }

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data");
        }

        return bookings
                .stream()
                .map(booking -> BookingDto.builder()
                        .id(booking.getId())
                        .start(booking.getStart())
                        .end(booking.getEnd())
                        .item(cs.convert(booking.getItem(), ItemDto.class))
                        .booker(cs.convert(booking.getBooker(), UserDto.class))
                        .status(booking.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(String state, Long userId) {
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookingsForOwner(userId);
                break;
            case "PAST":
                bookings = bookingRepository.findPastBookingsForOwner(userId);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureBookingsForOwner(userId);
                break;
            case "WAITING":
                bookings = bookingRepository.findBookingsByStatusAndBookerIdForOwner(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findBookingsByStatusAndBookerIdForOwner(userId, BookingStatus.REJECTED);
                break;
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(userId, Sort.by(Sort.Direction.DESC, "id"));
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        }

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data");
        }

        return bookings
                .stream()
                .map(booking -> BookingDto.builder()
                        .id(booking.getId())
                        .start(booking.getStart())
                        .end(booking.getEnd())
                        .item(cs.convert(booking.getItem(), ItemDto.class))
                        .booker(cs.convert(booking.getBooker(), UserDto.class))
                        .status(booking.getStatus())
                        .build())
                .toList();
    }

}
