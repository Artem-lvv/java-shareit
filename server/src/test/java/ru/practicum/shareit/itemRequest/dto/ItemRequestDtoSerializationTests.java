package ru.practicum.shareit.itemRequest.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.model.item.dto.ItemIdNameIdOwnerDto;
import ru.practicum.shareit.itemRequest.model.dto.CreateItemRequestDto;
import ru.practicum.shareit.itemRequest.model.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.model.dto.ItemRequestWithItemsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class ItemRequestDtoSerializationTests {

    private JacksonTester<CreateItemRequestDto> createItemRequestDtoJson;
    private JacksonTester<ItemRequestDto> itemRequestDtoJson;
    private JacksonTester<ItemRequestWithItemsDto> itemRequestWithItemsDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void createItemRequestDtoSerialization() throws Exception {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto("Need a new drill");

        JsonContent<CreateItemRequestDto> jsonContent = createItemRequestDtoJson.write(createItemRequestDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.description", "Need a new drill");
    }

    @Test
    void itemRequestDtoSerialization() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Looking for a tent")
                .created(LocalDateTime.of(2024, 8, 3, 22, 40, 37))
                .build();

        JsonContent<ItemRequestDto> jsonContent = itemRequestDtoJson.write(itemRequestDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.description", "Looking for a tent");
        assertThat(jsonContent).hasJsonPathValue("$.created", "2024-08-03T22:40:37");
    }

    @Test
    void itemRequestWithItemsDtoSerialization() throws Exception {
        ItemIdNameIdOwnerDto itemDto = new ItemIdNameIdOwnerDto(1L, "Tent", 2L);
        List<ItemIdNameIdOwnerDto> items = List.of(itemDto);

        ItemRequestWithItemsDto itemRequestWithItemsDto = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("Looking for a tent")
                .created(LocalDateTime.of(2024, 8, 3, 22, 40, 37))
                .items(items)
                .build();

        JsonContent<ItemRequestWithItemsDto> jsonContent = itemRequestWithItemsDtoJson.write(itemRequestWithItemsDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.description", "Looking for a tent");
        assertThat(jsonContent).hasJsonPathValue("$.created", "2024-08-03T22:40:37");
        assertThat(jsonContent).hasJsonPathValue("$.items[0].id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.items[0].name", "Tent");
        assertThat(jsonContent).hasJsonPathValue("$.items[0].ownerId", 2L);
    }
}
