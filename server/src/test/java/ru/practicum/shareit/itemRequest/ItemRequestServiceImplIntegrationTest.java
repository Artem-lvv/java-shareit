package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ItemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.ItemRequest.repository.ItemRequestRepository;
import ru.practicum.shareit.ItemRequest.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setup() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createItemRequest() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a drill");
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(createItemRequestDto, user.getId());

        assertThat(itemRequestDto).isNotNull();
        assertThat(itemRequestDto.description()).isEqualTo("Need a drill");
    }

    @Test
    void createItemRequest_UserNotFound() {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a drill");

        assertThrows(EntityNotFoundByIdException.class, () -> {
            itemRequestService.createItemRequest(createItemRequestDto, 999L);
        });
    }

    @Test
    void getRequestsByUserId() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a drill");
        itemRequestService.createItemRequest(createItemRequestDto, user.getId());

        List<ItemRequestWithItemsDto> requests = itemRequestService.getRequestsByUserId(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).description()).isEqualTo("Need a drill");
    }

    @Test
    void getRequestsById() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a drill");
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(createItemRequestDto, user.getId());

        ItemRequestWithItemsDto request = itemRequestService.getRequestsById(itemRequestDto.id());

        assertThat(request).isNotNull();
        assertThat(request.description()).isEqualTo("Need a drill");
    }

    @Test
    void getAllByPage() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userRepository.save(user);

        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a drill");
        itemRequestService.createItemRequest(createItemRequestDto, user.getId());

        List<ItemRequestDto> requests = itemRequestService.getAllByPage(0, 10);

        assertThat(requests).hasSize(1);
    }
}
