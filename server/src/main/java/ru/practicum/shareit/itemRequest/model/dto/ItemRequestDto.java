package ru.practicum.shareit.itemRequest.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemRequestDto(
        Long id,
        String description,
        LocalDateTime created
        ) {
}
