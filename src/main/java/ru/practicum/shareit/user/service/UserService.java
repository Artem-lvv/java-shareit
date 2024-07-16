package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUser(final Long userId, UpdateUserDto updateUserDto);

    void deleteUser(final Long userId);

    UserDto getUserById(final Long userId);

    List<UserDto> getAllUsers();
}
