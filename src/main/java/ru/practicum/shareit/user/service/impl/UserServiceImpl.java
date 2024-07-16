package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        User newUser = cs.convert(createUserDto, User.class);
        if (Objects.isNull(newUser)) {
            throw new InternalServerException("Failed created user");
        }

        newUser = userRepository.save(newUser);
        log.info("Create user {}", newUser);

        return cs.convert(newUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(final Long userId, UpdateUserDto updateUserDto) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty()) {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }

        if (Objects.nonNull(updateUserDto.email())) {
            Optional<User> userByEmail = userRepository.findByEmail(updateUserDto.email());
            if (userByEmail.isPresent() && !userId.equals(userByEmail.get().getId())) {
                throw new EntityDuplicateException("Email", updateUserDto.email());
            }

            userById.get().setEmail(updateUserDto.email());
        }

        if (Objects.nonNull(updateUserDto.name())) {
            userById.get().setName(updateUserDto.name());
        }

        User updateUser = userRepository.save(userById.get());
        log.info("Update user {}", updateUser);

        return cs.convert(updateUser, UserDto.class);
    }

    @Override
    public void deleteUser(final Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            log.info("Delete user by id: {}", userId);
        }
    }

    @Override
    public UserDto getUserById(final Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            return cs.convert(userById.get(), UserDto.class);
        } else {
            throw new EntityNotFoundByIdException("User", userId.toString());
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> cs.convert(user, UserDto.class))
                .toList();
    }
}
