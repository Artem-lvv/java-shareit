package ru.practicum.shareit.item.model.dto;

public record UpdateItemDto(
        String name,
        String description,
        Boolean available
){
}
