package ru.practicum.shareit.item.model.item.dto;

import lombok.Builder;

@Builder
public record CreateItemDto(
        String name,
        String description,
        Boolean available,
        Long requestId
) {
}
