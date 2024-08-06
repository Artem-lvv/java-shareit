package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.TypeState;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private CreateBookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Long userId = 1L;
        createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(new User());

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(createBookingDto.start());
        booking.setEnd(createBookingDto.end());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(createBookingDto.start())
                .end(createBookingDto.end())
                .item(null)
                .booker(null)
                .status(BookingStatus.WAITING)
                .build();
    }



    @Test
    void testCreateBookingSuccess() {


        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(createBookingDto.itemId())).thenReturn(Optional.of(item));
        when(conversionService.convert(createBookingDto, Booking.class)).thenReturn(booking);
        when(conversionService.convert(booking, BookingDto.class)).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(createBookingDto, user.getId());

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBookingUserNotFound() {
        Long userId = 1L;
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundByIdException exception = assertThrows(EntityNotFoundByIdException.class,
                () -> bookingService.createBooking(createBookingDto, userId));

        assertEquals("No entity [User] with id: [1]", exception.getReason());
    }

    @Test
    void testCreateBookingItemNotFound() {
        Long userId = 1L;
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(createBookingDto.itemId())).thenReturn(Optional.empty());

        EntityNotFoundByIdException exception = assertThrows(EntityNotFoundByIdException.class,
                () -> bookingService.createBooking(createBookingDto, userId));

        assertEquals("No entity [Item] with id: [1]", exception.getReason());
    }

    @Test
    void testCreateBookingItemUnavailable() {
        Long userId = 1L;
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(createBookingDto.itemId())).thenReturn(Optional.of(item));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookingService.createBooking(createBookingDto, userId));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testUpdateBookingStatus() {
        Long bookingId = 1L;
        Long userId = 1L;
        boolean approved = true;

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findByIdAndItemOwnerId(bookingId, userId)).thenReturn(Optional.of(booking));
        when(conversionService.convert(booking, BookingDto.class)).thenReturn(bookingDto);

        BookingDto result = bookingService.updateBookingStatus(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void testUpdateBookingStatusNotFound() {
        Long bookingId = 1L;
        Long userId = 1L;
        boolean approved = true;

        when(bookingRepository.findByIdAndItemOwnerId(bookingId, userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookingService.updateBookingStatus(bookingId, approved, userId));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testGetBooking() {
        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.WAITING)
                .booker(null)
                .item(null)
                .build();

        when(bookingRepository.findBookingByBookingIdAndBookerIdOrItemOwnerId(bookingId, userId)).thenReturn(Optional.of(booking));
        when(conversionService.convert(booking, BookingDto.class)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBooking(bookingId, userId);

        assertNotNull(result);
    }

    @Test
    void testGetBookingNotFound() {
        Long bookingId = 1L;
        Long userId = 1L;

        when(bookingRepository.findBookingByBookingIdAndBookerIdOrItemOwnerId(bookingId, userId)).thenReturn(Optional.empty());

        EntityNotFoundByIdException exception = assertThrows(EntityNotFoundByIdException.class,
                () -> bookingService.getBooking(bookingId, userId));

        assertEquals("No entity [Booking] with id: [1]", exception.getReason());
    }

    @Test
    void testGetBookings() {

        TypeState state = TypeState.ALL;

        when(bookingRepository.findAllByBooker_Id(user.getId(), Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of(booking));
        when(conversionService.convert(booking, BookingDto.class)).thenReturn(bookingDto);

        List<BookingDto> result = bookingService.getBookings(state, user.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetBookingsNoData() {
        Long userId = 1L;
        TypeState state = TypeState.ALL;

        when(bookingRepository.findAllByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookingService.getBookings(state, userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testGetBookingsForOwner() {
        TypeState state = TypeState.ALL;

        when(bookingRepository.findAllByItemOwnerId(user.getId(), Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of(booking));
        when(conversionService.convert(booking, BookingDto.class)).thenReturn(bookingDto);

        List<BookingDto> result = bookingService.getBookingsForOwner(state, user.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetBookingsForOwnerNoData() {
        Long userId = 1L;
        TypeState state = TypeState.ALL;

        when(bookingRepository.findAllByItemOwnerId(userId, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookingService.getBookingsForOwner(state, userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
