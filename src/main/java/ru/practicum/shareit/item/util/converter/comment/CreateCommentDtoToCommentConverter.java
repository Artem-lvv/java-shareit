package ru.practicum.shareit.item.util.converter.comment;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;

@Component
public class CreateCommentDtoToCommentConverter implements Converter<CreateCommentDto, Comment> {
    @Override
    public Comment convert(CreateCommentDto src) {
        return Comment.builder()
                .text(src.text())
                .build();
    }
}
