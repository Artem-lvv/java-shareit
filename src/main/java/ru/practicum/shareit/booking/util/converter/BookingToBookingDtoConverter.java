package ru.practicum.shareit.booking.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.Objects;

@Component
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    @Override
    public BookingDto convert(Booking src) {
        return BookingDto.builder()
                .id(src.getId())
                .start(src.getStart())
                .end(src.getEnd())
                .status(src.getStatus())
                .item(Objects.nonNull(src.getItem()) ? ItemDto.builder()
                        .id(src.getItem().getId())
                        .name(src.getItem().getName())
                        .description(src.getItem().getDescription())
                        .available(src.getItem().isAvailable())
                        .build()
                        : null)
                .booker(Objects.nonNull(src.getBooker()) ? UserDto.builder()
                        .id(src.getBooker().getId())
                        .name(src.getBooker().getName())
                        .email(src.getBooker().getEmail())
                        .build()
                        : null)
                .build();
    }
}
