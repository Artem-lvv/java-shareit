package ru.practicum.shareit.item.model.item.dto;

public record UpdateItemDto(
        String name,
        String description,
        Boolean available
){
}
