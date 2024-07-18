package ru.practicum.shareit.item.model.item.dto;

import lombok.Builder;

@Builder
public record ItemDto(
        Long id,
        String name,
        String description,
        boolean available
) {
}
