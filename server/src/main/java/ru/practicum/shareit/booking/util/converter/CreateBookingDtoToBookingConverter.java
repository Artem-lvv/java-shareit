package ru.practicum.shareit.booking.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;

@Component
public class CreateBookingDtoToBookingConverter implements Converter<CreateBookingDto, Booking> {
    @Override
    public Booking convert(CreateBookingDto src) {
        return Booking.builder()
                .start(src.start())
                .end(src.end())
                .status(src.status())
                .build();
    }
}
