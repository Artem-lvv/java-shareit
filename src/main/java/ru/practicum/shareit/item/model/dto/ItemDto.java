package ru.practicum.shareit.item.model.dto;

import lombok.Builder;

/**
 * TODO Sprint add-controllers.
 */

@Builder
public record ItemDto(
        Long id,
        String name,
        String description,
        boolean available
) {

}
