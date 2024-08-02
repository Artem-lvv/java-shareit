package ru.practicum.shareit.item.model.item.dto;

import lombok.Builder;

@Builder
public record ItemIdNameIdOwnerDto(
        Long id,
        String name,
        Long ownerId
){
}
