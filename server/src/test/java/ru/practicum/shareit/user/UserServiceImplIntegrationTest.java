package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityDuplicateException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john.doe@example.com");
        UserDto userDto = userService.createUser(createUserDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.name()).isEqualTo("John Doe");
        assertThat(userDto.email()).isEqualTo("john.doe@example.com");
    }

    @Test
    void createUser_DuplicateEmail() {
        CreateUserDto createUserDto1 = new CreateUserDto("John Doe", "john.doe@example.com");
        userService.createUser(createUserDto1);

        CreateUserDto createUserDto2 = new CreateUserDto("Jane Doe", "john.doe@example.com");

        assertThrows(EntityDuplicateException.class, () -> {
            userService.createUser(createUserDto2);
        });
    }

    @Test
    void updateUser() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john.doe@example.com");
        UserDto userDto = userService.createUser(createUserDto);

        UpdateUserDto updateUserDto = new UpdateUserDto("Johnny Doe", "johnny.doe@example.com");
        UserDto updatedUserDto = userService.updateUser(userDto.id(), updateUserDto);

        assertThat(updatedUserDto.name()).isEqualTo("Johnny Doe");
        assertThat(updatedUserDto.email()).isEqualTo("johnny.doe@example.com");
    }

    @Test
    void deleteUser() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john.doe@example.com");
        UserDto userDto = userService.createUser(createUserDto);

        userService.deleteUser(userDto.id());

        Optional<User> deletedUser = userRepository.findById(userDto.id());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void getUserById() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john.doe@example.com");
        UserDto userDto = userService.createUser(createUserDto);

        UserDto fetchedUserDto = userService.getUserById(userDto.id());

        assertThat(fetchedUserDto).isNotNull();
        assertThat(fetchedUserDto.id()).isEqualTo(userDto.id());
        assertThat(fetchedUserDto.name()).isEqualTo(userDto.name());
        assertThat(fetchedUserDto.email()).isEqualTo(userDto.email());
    }

    @Test
    void getAllUsers() {
        CreateUserDto createUserDto1 = new CreateUserDto("John Doe", "john.doe@example.com");
        CreateUserDto createUserDto2 = new CreateUserDto("Jane Doe", "jane.doe@example.com");

        userService.createUser(createUserDto1);
        userService.createUser(createUserDto2);

        assertThat(userService.getAllUsers()).hasSize(2);
    }

}
