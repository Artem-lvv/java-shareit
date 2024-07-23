package ru.practicum.shareit.item.model.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record CreateItemDto(
        @Size(max = 50, message = "The Name field must be no more than 50 characters")
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String name,
        @Size(max = 250, message = "The Description field must be no more than 250 characters")
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String description,
        @NonNull
        Boolean available
) {
}
