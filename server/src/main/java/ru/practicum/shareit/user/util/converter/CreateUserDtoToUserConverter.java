package ru.practicum.shareit.user.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;

@Component
public class CreateUserDtoToUserConverter implements Converter<CreateUserDto, User> {
    @Override
    public User convert(CreateUserDto src) {
        return User.builder()
                .name(src.name())
                .email(src.email())
                .build();
    }
}
