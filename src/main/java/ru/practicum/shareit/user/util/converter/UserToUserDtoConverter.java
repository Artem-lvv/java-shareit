package ru.practicum.shareit.user.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User scr) {
        return UserDto.builder()
                .id(scr.getId())
                .name(scr.getName())
                .email(scr.getEmail())
                .build();
    }
}
