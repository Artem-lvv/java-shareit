package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.TypeState;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Transactional
    @Override
    public BookingDto createBooking(CreateBookingDto createBookingDto, final Long userId) {
        User userById = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundByIdException("User", userId.toString()));

        Optional<Item> itemById = itemRepository.findById(createBookingDto.itemId());
        if (itemById.isEmpty()) {
            throw new EntityNotFoundByIdException("Item", createBookingDto.itemId().toString());
        } else if (!itemById.get().isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Item by id [%s] not found or is unavailable".formatted(createBookingDto.itemId()));
        }

        if (itemById.get().getOwner().equals(userById)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The owner of the thing is equal to the customer");
        }

        final Booking newBooking = cs.convert(createBookingDto, Booking.class);
        newBooking.setBooker(userById);
        newBooking.setItem(itemById.get());
        newBooking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(newBooking);
        log.info("Create booking {}", newBooking);

        return cs.convert(newBooking, BookingDto.class);
    }

    @Transactional
    @Override
    public BookingDto updateBookingStatus(final Long bookingId, final boolean approved, final Long userId) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Entity not found by id [%s]".formatted(bookingId.toString())));

        BookingStatus newBookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        if (booking.getStatus() == newBookingStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current status is equal to the new one");
        }

        booking.setStatus(newBookingStatus);
        bookingRepository.save(booking);
        log.info("Update biiking {}", booking);

        return cs.convert(booking, BookingDto.class);
    }

    @Override
    public BookingDto getBooking(final Long bookingId, final Long userId) {
        Booking booking = bookingRepository.findBookingByBookingIdAndBookerIdOrItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundByIdException("Booking", bookingId.toString()));

        return cs.convert(booking, BookingDto.class);
    }

    @Override
    public List<BookingDto> getBookings(final TypeState state, final Long userId) {
        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findCurrentBookings(userId);
            case PAST -> bookingRepository.findPastBookings(userId);
            case FUTURE -> bookingRepository.findFutureBookings(userId);
            case WAITING -> bookingRepository.findBookingsByStatusAndBookerId(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findBookingsByStatusAndBookerId(userId, BookingStatus.REJECTED);
            case ALL -> bookingRepository.findAllByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "id"));
            default -> throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        };

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data");
        }

        return bookings
                .stream()
                .map(booking -> cs.convert(booking, BookingDto.class))
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(TypeState state, Long userId) {
        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findCurrentBookingsForOwner(userId);
            case PAST -> bookingRepository.findPastBookingsForOwner(userId);
            case FUTURE -> bookingRepository.findFutureBookingsForOwner(userId);
            case WAITING -> bookingRepository.findBookingsByStatusAndBookerIdForOwner(userId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findBookingsByStatusAndBookerIdForOwner(userId, BookingStatus.REJECTED);
            case ALL -> bookingRepository.findAllByItemOwnerId(userId, Sort.by(Sort.Direction.DESC, "id"));
            default -> throw new UnsupportedStatusException("Unknown state: %s".formatted(state));
        };

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data");
        }

        return bookings
                .stream()
                .map(booking -> cs.convert(booking, BookingDto.class))
                .toList();
    }

}
