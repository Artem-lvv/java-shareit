package ru.practicum.shareit.user.model.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String name,
        String email
) {
}
