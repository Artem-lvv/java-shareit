package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;

public interface CommentService {
    CommentDto createComment(final CreateCommentDto createCommentDto, final Long itemId, final Long userId);
}
