package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.comment.impl.CommentServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CommentServiceImplIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createComment() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        CreateCommentDto createCommentDto = new CreateCommentDto("Great drill!");
        CommentDto commentDto = commentService.createComment(createCommentDto, item.getId(), user.getId());

        assertThat(commentDto).isNotNull();
        assertThat(commentDto.text()).isEqualTo("Great drill!");
        assertThat(commentDto.authorName()).isEqualTo(user.getName());
    }

    @Test
    void createComment_NoPastBooking() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        User owner = new User();
        owner.setName("Jane Doe");
        owner.setEmail("jane.doe@example.com");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("A powerful drill");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        CreateCommentDto createCommentDto = new CreateCommentDto("Great drill!");

        assertThrows(ResponseStatusException.class, () -> {
            commentService.createComment(createCommentDto, item.getId(), user.getId());
        });
    }
}
