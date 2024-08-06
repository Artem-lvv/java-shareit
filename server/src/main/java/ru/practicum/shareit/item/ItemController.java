package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.comment.CommentService;
import ru.practicum.shareit.item.service.item.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @ModelAttribute("userId")
    public Long getUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userId;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto,
                              @ModelAttribute("userId") Long userId) {
        return itemService.createItem(userId, createItemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody UpdateItemDto updateItemDto,
                              @PathVariable Long itemId,
                              @ModelAttribute("userId") Long userId) {
        return itemService.updateItem(itemId, userId, updateItemDto);
    }

    @GetMapping("/{itemId}")
    public ItemWithRelatedEntitiesDto findById(@PathVariable Long itemId, @ModelAttribute("userId") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemWithRelatedEntitiesDto> findItemsByOwnerId(@ModelAttribute("userId") Long userId) {
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.getItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CreateCommentDto createCommentDto,
                                    @PathVariable Long itemId,
                                    @ModelAttribute("userId") Long userId) {
        return commentService.createComment(createCommentDto, itemId, userId);
    }

}
