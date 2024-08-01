package ru.practicum.shareit.ItemRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.ItemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.ItemRequest.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @ModelAttribute("userId")
    public Long getUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userId;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody CreateItemRequestDto createItemRequestDto,
                                            @ModelAttribute("userId") Long userId) {
        return itemRequestService.createItemRequest(createItemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> findRequestsByUserId(@ModelAttribute("userId") Long userId) {
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findRequestsByUserIdAndRequestId(@ModelAttribute("userId") Long userId,
                                                                    @PathVariable Long requestId) {
        return itemRequestService.getRequestsById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findByPage(@RequestParam Integer from, @RequestParam Integer size) {
        return itemRequestService.getAllByPage(from, size);
    }

}
