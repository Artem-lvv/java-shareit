package ru.practicum.shareit.item.model.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CreateItemDto(
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String name,
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String description,
        @NonNull
        Boolean available
) {
}
