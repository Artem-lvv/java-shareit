package ru.practicum.shareit.item.util.converter.comment;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

@Component
public class CommentToCommentDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(Comment src) {
        return CommentDto.builder()
                .id(src.getId())
                .text(src.getText())
                .authorName(src.getAuthor().getName())
                .created(src.getCreated())
                .build();
    }
}
