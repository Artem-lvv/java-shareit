package ru.pracicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(max = 50, message = "Name field must be no more than 50 characters")
        String name,
        @Email(message = "Element has to be a well-formed email address")
        String email
) {
}
