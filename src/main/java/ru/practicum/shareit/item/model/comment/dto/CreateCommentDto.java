package ru.practicum.shareit.item.model.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        @Size(max = 250, message = "The Text field must be no more than 250 characters")
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String text
){
}
