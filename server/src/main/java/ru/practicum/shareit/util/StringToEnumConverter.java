package ru.practicum.shareit.util;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.model.TypeState;

public class StringToEnumConverter implements Converter<String, TypeState> {
    @Override
    public TypeState convert(String source) {
        try {
            return TypeState.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TypeState.UNSUPPORTED_STATUS;
        }
    }
}
