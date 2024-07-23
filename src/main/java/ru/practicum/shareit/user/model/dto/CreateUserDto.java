package ru.practicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @Size(max = 50, message = "Name field must be no more than 50 characters")
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String name,
        @NotEmpty
        @Email(message = "Element has to be a well-formed email address")
        String email
) {
}