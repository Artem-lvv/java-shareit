package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class BookingDtoSerializationTests {

    private JacksonTester<BookingDto> bookingDtoJson;
    private JacksonTester<BookingIdAndBookerIdDto> bookingIdAndBookerIdDtoJson;
    private JacksonTester<CreateBookingDto> createBookingDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void bookingDtoSerialization() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 8, 3, 22, 40, 37))
                .end(LocalDateTime.of(2024, 8, 4, 22, 40, 37))
                .item(new ItemDto(1L, "Item name", "Item description", true))
                .booker(new UserDto(1L, "John Doe", "john.doe@example.com"))
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> jsonContent = bookingDtoJson.write(bookingDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.start", "2024-08-03T22:40:37"); // Note: Time will not include nanoseconds
        assertThat(jsonContent).hasJsonPathValue("$.end", "2024-08-04T22:40:37");
        assertThat(jsonContent).hasJsonPathValue("$.item.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.item.name", "Item name");
        assertThat(jsonContent).hasJsonPathValue("$.item.description", "Item description");
        assertThat(jsonContent).hasJsonPathValue("$.item.available", true);
        assertThat(jsonContent).hasJsonPathValue("$.booker.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.booker.name", "John Doe");
        assertThat(jsonContent).hasJsonPathValue("$.booker.email", "john.doe@example.com");
        assertThat(jsonContent).hasJsonPathValue("$.status", "APPROVED");
    }

    @Test
    void bookingIdAndBookerIdDtoSerialization() throws Exception {
        BookingIdAndBookerIdDto bookingIdAndBookerIdDto = BookingIdAndBookerIdDto.builder()
                .id(1L)
                .bookerId(2L)
                .build();

        JsonContent<BookingIdAndBookerIdDto> jsonContent = bookingIdAndBookerIdDtoJson.write(bookingIdAndBookerIdDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.bookerId", 2L);
    }

    @Test
    void createBookingDtoSerialization() throws Exception {
        CreateBookingDto createBookingDto = CreateBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, 8, 3, 22, 40, 37))
                .end(LocalDateTime.of(2024, 8, 4, 22, 40, 37))
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<CreateBookingDto> jsonContent = createBookingDtoJson.write(createBookingDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.itemId", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.start", "2024-08-03T22:40:37"); // Note: Time will not include nanoseconds
        assertThat(jsonContent).hasJsonPathValue("$.end", "2024-08-04T22:40:37");
        assertThat(jsonContent).hasJsonPathValue("$.status", "WAITING");
    }

}
