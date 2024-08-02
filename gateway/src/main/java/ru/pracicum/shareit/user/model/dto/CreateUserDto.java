package ru.pracicum.shareit.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserDto(
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String name,
        @NotEmpty
        @Email(message = "Element has to be a well-formed email address")
        String email
) {
}