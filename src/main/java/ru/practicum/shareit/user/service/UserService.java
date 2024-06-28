package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUser(Long userId, UpdateUserDto updateUserDto);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();
}
