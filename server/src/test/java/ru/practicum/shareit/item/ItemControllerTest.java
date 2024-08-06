package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.comment.CommentService;
import ru.practicum.shareit.item.service.item.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;
    private ItemWithRelatedEntitiesDto itemWithRelatedEntitiesDto;
    private CommentDto commentDto;
    private CreateCommentDto createCommentDto;

    @BeforeEach
    public void setup() {
        itemDto = new ItemDto(1L, "Item name", "Item description", true);
        createItemDto = CreateItemDto.builder()
                .name("Item name")
                .description("Item description")
                .available(true)
                .build();
        updateItemDto = new UpdateItemDto("Updated name", "Updated description", true);
        itemWithRelatedEntitiesDto = ItemWithRelatedEntitiesDto.builder()
                .name("Item name")
                .id(1L)
                .description("Item description")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .build();
        commentDto = new CommentDto(1L, "Comment text", "Author name", LocalDateTime.now());
        createCommentDto = new CreateCommentDto("Comment text");
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(anyLong(), any(CreateItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.id()))
                .andExpect(jsonPath("$.name").value(itemDto.name()))
                .andExpect(jsonPath("$.description").value(itemDto.description()))
                .andExpect(jsonPath("$.available").value(itemDto.available()));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(UpdateItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.id()))
                .andExpect(jsonPath("$.name").value(itemDto.name()))
                .andExpect(jsonPath("$.description").value(itemDto.description()))
                .andExpect(jsonPath("$.available").value(itemDto.available()));
    }

    @Test
    void findById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemWithRelatedEntitiesDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemWithRelatedEntitiesDto.id()))
                .andExpect(jsonPath("$.name").value(itemWithRelatedEntitiesDto.name()))
                .andExpect(jsonPath("$.description").value(itemWithRelatedEntitiesDto.description()))
                .andExpect(jsonPath("$.available").value(itemWithRelatedEntitiesDto.available()));
    }

    @Test
    void findItemsByOwnerId() throws Exception {
        when(itemService.getAllItemsByOwnerId(anyLong())).thenReturn(List.of(itemWithRelatedEntitiesDto));

        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemWithRelatedEntitiesDto.id()))
                .andExpect(jsonPath("$[0].name").value(itemWithRelatedEntitiesDto.name()))
                .andExpect(jsonPath("$[0].description").value(itemWithRelatedEntitiesDto.description()))
                .andExpect(jsonPath("$[0].available").value(itemWithRelatedEntitiesDto.available()));
    }

    @Test
    void createComment() throws Exception {
        when(commentService.createComment(any(CreateCommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.id()))
                .andExpect(jsonPath("$.text").value(commentDto.text()))
                .andExpect(jsonPath("$.authorName").value(commentDto.authorName()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }
}
