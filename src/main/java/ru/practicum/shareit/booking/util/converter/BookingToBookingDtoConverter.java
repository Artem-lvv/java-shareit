package ru.practicum.shareit.booking.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;

@Component
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    @Override
    public BookingDto convert(Booking src) {
        return BookingDto.builder()
                .id(src.getId())
                .start(src.getStart())
                .end(src.getEnd())
                .status(src.getStatus())
                .build();
    }
}
