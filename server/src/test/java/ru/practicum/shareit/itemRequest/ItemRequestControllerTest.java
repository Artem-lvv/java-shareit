package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ItemRequest.ItemRequestController;
import ru.practicum.shareit.ItemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.ItemRequest.model.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.ItemRequest.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequestDto itemRequestDto;
    private CreateItemRequestDto createItemRequestDto;
    private ItemRequestWithItemsDto itemRequestWithItemsDto;

    @BeforeEach
    public void setup() {
        itemRequestDto = new ItemRequestDto(1L, "Request description", null);
        createItemRequestDto = new CreateItemRequestDto("Request description");
        itemRequestWithItemsDto = new ItemRequestWithItemsDto(1L, "Request description", null, List.of());
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(CreateItemRequestDto.class), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createItemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.id()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.description()));
    }

    @Test
    void findRequestsByUserId() throws Exception {
        when(itemRequestService.getRequestsByUserId(anyLong())).thenReturn(List.of(itemRequestWithItemsDto));

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestWithItemsDto.id()))
                .andExpect(jsonPath("$[0].description").value(itemRequestWithItemsDto.description()));
    }

    @Test
    void findRequestsByUserIdAndRequestId() throws Exception {
        when(itemRequestService.getRequestsById(anyLong())).thenReturn(itemRequestWithItemsDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestWithItemsDto.id()))
                .andExpect(jsonPath("$.description").value(itemRequestWithItemsDto.description()));
    }

}
