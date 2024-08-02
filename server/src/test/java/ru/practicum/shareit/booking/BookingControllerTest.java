package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.TypeState;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private CreateBookingDto createBookingDto;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    public void setup() {
        ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description", true);
        UserDto userDto = new UserDto(1L, "Test User", "testuser@example.com");

        LocalDateTime start = LocalDateTime.of(2024, 8, 3, 22, 40, 37);
        LocalDateTime end = LocalDateTime.of(2024, 8, 4, 22, 40, 37);


        bookingDto = new BookingDto(
                1L,
                start,
                end,
                itemDto,
                userDto,
                BookingStatus.APPROVED
        );

        createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void createBooking() throws Exception {
        Mockito.when(bookingService.createBooking(any(CreateBookingDto.class), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.id()))
                .andExpect(jsonPath("$.start").value(bookingDto.start().format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingDto.end().format(formatter)))
                .andExpect(jsonPath("$.item.id").value(bookingDto.item().id()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.item().name()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.booker().id()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.booker().name()))
                .andExpect(jsonPath("$.status").value(bookingDto.status().toString()));
    }

    @Test
    void updateBookingStatus() throws Exception {
        Mockito.when(bookingService.updateBookingStatus(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.id()))
                .andExpect(jsonPath("$.start").value(bookingDto.start().format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingDto.end().format(formatter)))
                .andExpect(jsonPath("$.item.id").value(bookingDto.item().id()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.item().name()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.booker().id()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.booker().name()))
                .andExpect(jsonPath("$.status").value(bookingDto.status().toString()));
    }

    @Test
    void getBooking() throws Exception {
        Mockito.when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.id()))
                .andExpect(jsonPath("$.start").value(bookingDto.start().format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingDto.end().format(formatter)))
                .andExpect(jsonPath("$.item.id").value(bookingDto.item().id()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.item().name()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.booker().id()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.booker().name()))
                .andExpect(jsonPath("$.status").value(bookingDto.status().toString()));
    }

    @Test
    void getBookings() throws Exception {
        Mockito.when(bookingService.getBookings(any(TypeState.class), anyLong())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.id()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.start().format(formatter)))
                .andExpect(jsonPath("$[0].end").value(bookingDto.end().format(formatter)))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.item().id()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.item().name()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.booker().id()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.booker().name()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.status().toString()));
    }

    @Test
    void getBookingsForOwner() throws Exception {
        Mockito.when(bookingService.getBookingsForOwner(any(TypeState.class), anyLong())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.id()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.start().format(formatter)))
                .andExpect(jsonPath("$[0].end").value(bookingDto.end().format(formatter)))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.item().id()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.item().name()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.booker().id()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.booker().name()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.status().toString()));
    }
}
