package ru.pracicum.shareit.item.model.item.dto;

import jakarta.validation.constraints.Size;

public record UpdateItemDto(
        @Size(max = 50, message = "The Name field must be no more than 50 characters")
        String name,
        @Size(max = 250, message = "The Description field must be no more than 250 characters")
        String description,
        Boolean available
){
}
