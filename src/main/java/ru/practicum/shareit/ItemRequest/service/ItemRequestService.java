package ru.practicum.shareit.ItemRequest.service;

import ru.practicum.shareit.ItemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(final CreateItemRequestDto createItemRequestDto, final Long userId);

    List<ItemRequestWithItemsDto> getRequestsByUserId(final Long userId);

    ItemRequestWithItemsDto getRequestsById(final Long requestId);

    List<ItemRequestDto> getAllByPage(final Integer from, final Integer size);
}
