package ru.pracicum.shareit.itemRequest.model.dto;

import jakarta.validation.constraints.Size;

public record CreateItemRequestDto(
        @Size(max = 500, message = "The Description field must be no more than 500 characters")
        String description
) {
}
