package ru.pracicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pracicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.pracicum.shareit.item.model.item.dto.CreateItemDto;
import ru.pracicum.shareit.item.model.item.dto.UpdateItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @ModelAttribute("userId")
    public Long getUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userId;
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody CreateItemDto createItemDto,
                                     @ModelAttribute("userId") Long userId) {
        return itemClient.createItem(userId, createItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody UpdateItemDto updateItemDto,
                              @PathVariable Long itemId,
                              @ModelAttribute("userId") Long userId) {
        return itemClient.updateItem(itemId, userId, updateItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId, @ModelAttribute("userId") Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByOwnerId(@ModelAttribute("userId") Long userId) {
        return itemClient.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @ModelAttribute("userId") Long userId) {
        return itemClient.getItemsByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CreateCommentDto createCommentDto,
                                    @PathVariable Long itemId,
                                    @ModelAttribute("userId") Long userId) {
        return itemClient.createComment(createCommentDto, itemId, userId);
    }

}
