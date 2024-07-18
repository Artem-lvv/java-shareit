package ru.practicum.shareit.item.model.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentDto(
        @NotBlank(message = "Element must not be null and must contain at least one non-whitespace character.")
        String text
){
}
