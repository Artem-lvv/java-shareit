package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.item.dto.CreateItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemDto;
import ru.practicum.shareit.item.model.item.dto.ItemIdNameIdOwnerDto;
import ru.practicum.shareit.item.model.item.dto.ItemWithRelatedEntitiesDto;
import ru.practicum.shareit.item.model.item.dto.UpdateItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class ItemDtoSerializationTests {

    private JacksonTester<CreateItemDto> createItemDtoJson;
    private JacksonTester<ItemDto> itemDtoJson;
    private JacksonTester<ItemIdNameIdOwnerDto> itemIdNameIdOwnerDtoJson;
    private JacksonTester<ItemWithRelatedEntitiesDto> itemWithRelatedEntitiesDtoJson;
    private JacksonTester<UpdateItemDto> updateItemDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void createItemDtoSerialization() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("Drill", "Electric drill", true, 1L);

        JsonContent<CreateItemDto> jsonContent = createItemDtoJson.write(createItemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "Drill");
        assertThat(jsonContent).hasJsonPathValue("$.description", "Electric drill");
        assertThat(jsonContent).hasJsonPathValue("$.available", true);
        assertThat(jsonContent).hasJsonPathValue("$.requestId", 1L);
    }

    @Test
    void itemDtoSerialization() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Drill", "Electric drill", true);

        JsonContent<ItemDto> jsonContent = itemDtoJson.write(itemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "Drill");
        assertThat(jsonContent).hasJsonPathValue("$.description", "Electric drill");
        assertThat(jsonContent).hasJsonPathValue("$.available", true);
    }

    @Test
    void itemIdNameIdOwnerDtoSerialization() throws Exception {
        ItemIdNameIdOwnerDto itemIdNameIdOwnerDto = new ItemIdNameIdOwnerDto(1L, "Tent", 2L);

        JsonContent<ItemIdNameIdOwnerDto> jsonContent = itemIdNameIdOwnerDtoJson.write(itemIdNameIdOwnerDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "Tent");
        assertThat(jsonContent).hasJsonPathValue("$.ownerId", 2L);
    }

    @Test
    void itemWithRelatedEntitiesDtoSerialization() throws Exception {
        BookingIdAndBookerIdDto lastBooking = new BookingIdAndBookerIdDto(1L, 2L);
        BookingIdAndBookerIdDto nextBooking = new BookingIdAndBookerIdDto(3L, 4L);
        CommentDto comment = new CommentDto(1L, "Great item!", "John", LocalDateTime.now());
        List<CommentDto> comments = List.of(comment);

        ItemWithRelatedEntitiesDto itemWithRelatedEntitiesDto = ItemWithRelatedEntitiesDto.builder()
                .id(1L)
                .name("Tent")
                .description("Camping tent")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();

        JsonContent<ItemWithRelatedEntitiesDto> jsonContent = itemWithRelatedEntitiesDtoJson.write(itemWithRelatedEntitiesDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "Tent");
        assertThat(jsonContent).hasJsonPathValue("$.description", "Camping tent");
        assertThat(jsonContent).hasJsonPathValue("$.available", true);
        assertThat(jsonContent).hasJsonPathValue("$.lastBooking.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.nextBooking.id", 3L);
        assertThat(jsonContent).hasJsonPathValue("$.comments[0].text", "Great item!");
    }

    @Test
    void updateItemDtoSerialization() throws Exception {
        UpdateItemDto updateItemDto = new UpdateItemDto("Updated Drill", "Updated description", false);

        JsonContent<UpdateItemDto> jsonContent = updateItemDtoJson.write(updateItemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "Updated Drill");
        assertThat(jsonContent).hasJsonPathValue("$.description", "Updated description");
        assertThat(jsonContent).hasJsonPathValue("$.available", false);
    }
}
