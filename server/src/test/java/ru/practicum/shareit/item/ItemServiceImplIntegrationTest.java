package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.item.impl.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void createItem() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemDto createItemDto = new CreateItemDto("Drill", "A powerful drill", true, null);
        ItemDto itemDto = itemService.createItem(user.getId(), createItemDto);

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.name()).isEqualTo("Drill");
        assertThat(itemDto.description()).isEqualTo("A powerful drill");
        assertThat(itemDto.available()).isTrue();
    }

    @Test
    void createItem_UserNotFound() {
        CreateItemDto createItemDto = new CreateItemDto("Drill", "A powerful drill", true, null);

        assertThrows(EntityNotFoundByIdException.class, () -> {
            itemService.createItem(999L, createItemDto);
        });
    }

    @Test
    void updateItem() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemDto createItemDto = new CreateItemDto("Drill", "A powerful drill", true, null);
        ItemDto itemDto = itemService.createItem(user.getId(), createItemDto);

        UpdateItemDto updateItemDto = new UpdateItemDto("Cordless Drill", "A powerful cordless drill", true);
        ItemDto updatedItemDto = itemService.updateItem(itemDto.id(), user.getId(), updateItemDto);

        assertThat(updatedItemDto.name()).isEqualTo("Cordless Drill");
        assertThat(updatedItemDto.description()).isEqualTo("A powerful cordless drill");
    }

    @Test
    void updateItem_ItemNotFound() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        UpdateItemDto updateItemDto = new UpdateItemDto("Cordless Drill", "A powerful cordless drill", true);

        assertThrows(EntityNotFoundByIdException.class, () -> {
            itemService.updateItem(999L, user.getId(), updateItemDto);
        });
    }

    @Test
    void getItemById() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemDto createItemDto = new CreateItemDto("Drill", "A powerful drill", true, null);
        ItemDto itemDto = itemService.createItem(user.getId(), createItemDto);

        ItemWithRelatedEntitiesDto fetchedItemDto = itemService.getItemById(itemDto.id(), user.getId());

        assertThat(fetchedItemDto).isNotNull();
        assertThat(fetchedItemDto.id()).isEqualTo(itemDto.id());
        assertThat(fetchedItemDto.name()).isEqualTo(itemDto.name());
        assertThat(fetchedItemDto.description()).isEqualTo(itemDto.description());
    }

    @Test
    void getAllItemsByOwnerId() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemDto createItemDto1 = new CreateItemDto("Drill", "A powerful drill", true, null);
        CreateItemDto createItemDto2 = new CreateItemDto("Hammer", "A sturdy hammer", true, null);

        itemService.createItem(user.getId(), createItemDto1);
        itemService.createItem(user.getId(), createItemDto2);

        assertThat(itemService.getAllItemsByOwnerId(user.getId())).hasSize(2);
    }

    @Test
    void getItemsByText() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemDto createItemDto1 = new CreateItemDto("Drill", "A powerful drill", true, null);
        CreateItemDto createItemDto2 = new CreateItemDto("Hammer", "A sturdy hammer", true, null);

        itemService.createItem(user.getId(), createItemDto1);
        itemService.createItem(user.getId(), createItemDto2);

        assertThat(itemService.getItemsByText("drill")).hasSize(1);
        assertThat(itemService.getItemsByText("hammer")).hasSize(1);
    }
}
