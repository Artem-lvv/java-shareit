package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserDto(
        String name,
        @Email(message = "Element has to be a well-formed email address")
        String email
) {
}
