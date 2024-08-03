package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUserSuccessfully() {
        CreateUserDto createUserDto = new CreateUserDto("Test User", "test@test.com");
        User user = new User(1L, "Test User", "test@test.com");
        UserDto userDto = new UserDto(1L, "Test User", "test@test.com");

        when(conversionService.convert(createUserDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto createdUser = userService.createUser(createUserDto);

        assertNotNull(createdUser);
        assertEquals("test@test.com", createdUser.email());
        assertEquals("Test User", createdUser.name());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_ShouldThrowEntityDuplicateException() {
        CreateUserDto createUserDto = new CreateUserDto("Test User", "test@test.com");
        when(userRepository.findByEmail(createUserDto.email())).thenReturn(Optional.of(new User()));
        when(conversionService.convert(createUserDto, User.class))
                .thenReturn(User.builder()
                        .id(1L)
                        .email(createUserDto.email())
                        .name(createUserDto.name())
                        .build());

        assertThrows(EntityDuplicateException.class, () -> userService.createUser(createUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {
        Long userId = 1L;
        UpdateUserDto updateUserDto = new UpdateUserDto("Updated User", "updated@test.com");
        User user = new User(userId, "Test User", "test@test.com");
        User updatedUser = new User(userId, "Updated User", "updated@test.com");
        UserDto updatedUserDto = new UserDto(userId, "Updated User", "updated@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateUserDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(conversionService.convert(updatedUser, UserDto.class)).thenReturn(updatedUserDto);

        UserDto result = userService.updateUser(userId, updateUserDto);

        assertNotNull(result);
        assertEquals("updated@test.com", result.email());
        assertEquals("Updated User", result.name());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldThrowEntityNotFoundByIdException() {
        Long userId = 1L;
        UpdateUserDto updateUserDto = new UpdateUserDto("updated@test.com", "Updated User");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundByIdException.class, () -> userService.updateUser(userId, updateUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUserSuccessfully() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUserById_ShouldReturnUserSuccessfully() {
        Long userId = 1L;
        User user = new User(userId, "Test User", "test@test.com");
        UserDto userDto = new UserDto(userId, "Test User", "test@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("test@test.com", result.email());
        assertEquals("Test User", result.name());
    }

    @Test
    void getUserById_ShouldThrowEntityNotFoundByIdException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundByIdException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        User user = new User(1L, "Test User", "test@test.com");
        UserDto userDto = new UserDto(1L, "Test User", "test@test.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(conversionService.convert(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("test@test.com", result.get(0).email());
    }
}
