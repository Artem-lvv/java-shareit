package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBooking() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
        .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();
        BookingDto bookingDto = bookingService.createBooking(createBookingDto, user.getId());

        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.item().id()).isEqualTo(item.getId());
        assertThat(bookingDto.booker().id()).isEqualTo(user.getId());
        assertThat(bookingDto.status()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void createBooking_UserNotFound() {
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        assertThrows(EntityNotFoundByIdException.class, () -> {
            bookingService.createBooking(createBookingDto, 999L);
        });
    }

    @Test
    void updateBookingStatus() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();
        BookingDto bookingDto = bookingService.createBooking(createBookingDto, user.getId());

        BookingDto updatedBookingDto = bookingService.updateBookingStatus(bookingDto.id(), true, owner.getId());

        assertThat(updatedBookingDto.status()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void getBooking() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();
        BookingDto bookingDto = bookingService.createBooking(createBookingDto, user.getId());

        BookingDto fetchedBookingDto = bookingService.getBooking(bookingDto.id(), user.getId());

        assertThat(fetchedBookingDto).isNotNull();
        assertThat(fetchedBookingDto.id()).isEqualTo(bookingDto.id());
        assertThat(fetchedBookingDto.item().id()).isEqualTo(item.getId());
    }

    @Test
    void getBookings() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item.getId())
                .build();
        bookingService.createBooking(createBookingDto, user.getId());

        List<BookingDto> bookings = bookingService.getBookings(TypeState.ALL, user.getId());

        assertThat(bookings).hasSize(1);
    }
}
