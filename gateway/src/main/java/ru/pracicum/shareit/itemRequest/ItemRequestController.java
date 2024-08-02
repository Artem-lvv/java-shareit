package ru.pracicum.shareit.itemRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pracicum.shareit.itemRequest.model.dto.CreateItemRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @ModelAttribute("userId")
    public Long getUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userId;
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody CreateItemRequestDto createItemRequestDto,
                                            @ModelAttribute("userId") Long userId) {
        return itemRequestClient.createItemRequest(createItemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findRequestsByUserId(@ModelAttribute("userId") Long userId) {
        return itemRequestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestsByUserIdAndRequestId(@ModelAttribute("userId") Long userId,
                                                                    @PathVariable Long requestId) {
        return itemRequestClient.getRequestsById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findByPage(@RequestParam Integer from, @RequestParam Integer size) {
        return itemRequestClient.getAllByPage(from, size);
    }

}
