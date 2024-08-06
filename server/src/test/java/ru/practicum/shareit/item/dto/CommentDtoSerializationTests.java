package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class CommentDtoSerializationTests {

    private JacksonTester<CommentDto> commentDtoJson;
    private JacksonTester<CreateCommentDto> createCommentDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void commentDtoSerialization() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Nice item!")
                .authorName("Alice")
                .created(LocalDateTime.of(2024, 8, 3, 22, 40, 37))
                .build();

        JsonContent<CommentDto> jsonContent = commentDtoJson.write(commentDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.text", "Nice item!");
        assertThat(jsonContent).hasJsonPathValue("$.authorName", "Alice");
        assertThat(jsonContent).hasJsonPathValue("$.created", "2024-08-03T22:40:37");
    }

    @Test
    void createCommentDtoSerialization() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto("Great product!");

        JsonContent<CreateCommentDto> jsonContent = createCommentDtoJson.write(createCommentDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.text", "Great product!");
    }
}
