package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    public void setup() {
        userDto = new UserDto(1L, "John Doe", "john.doe@example.com");
        createUserDto = new CreateUserDto("John Doe", "john.doe@example.com");
        updateUserDto = new UpdateUserDto("John Doe", "john.doe@example.com");
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(CreateUserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(anyLong(), any(UpdateUserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.id()))
                .andExpect(jsonPath("$[0].name").value(userDto.name()))
                .andExpect(jsonPath("$[0].email").value(userDto.email()));
    }

    @Test
    void findById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }
}
