package ru.practicum.shareit.item.service.comment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public CommentDto createComment(CreateCommentDto createCommentDto, Long itemId, Long userId) {
        Optional<Booking> pastApprovedBooking = bookingRepository.findPastApprovedBooking(userId,
                itemId);

        if (pastApprovedBooking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not found user by id [%s] or item by id [%s] or not data booking");
        }

        Comment comment = cs.convert(createCommentDto, Comment.class);
        comment.setAuthor(pastApprovedBooking.get().getBooker());
        comment.setItem(pastApprovedBooking.get().getItem());
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);
        log.info("Create comment {}", comment);

        return cs.convert(comment, CommentDto.class);
    }
}
