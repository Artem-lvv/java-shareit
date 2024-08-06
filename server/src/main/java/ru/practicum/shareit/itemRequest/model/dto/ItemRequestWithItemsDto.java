package ru.practicum.shareit.itemRequest.model.dto;

import lombok.Builder;
import ru.practicum.shareit.item.model.item.dto.ItemIdNameIdOwnerDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ItemRequestWithItemsDto(
        Long id,
        String description,
        LocalDateTime created,
        List<ItemIdNameIdOwnerDto> items
) {

}
