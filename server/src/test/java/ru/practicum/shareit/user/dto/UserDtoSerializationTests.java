package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.model.dto.CreateUserDto;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@SpringJUnitConfig
class UserDtoSerializationTests {

    private JacksonTester<UserDto> userDtoJson;
    private JacksonTester<UpdateUserDto> updateUserDtoJson;
    private JacksonTester<CreateUserDto> createUserDtoJson;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void userDtoSerialization() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        JsonContent<UserDto> jsonContent = userDtoJson.write(userDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.id", 1L);
        assertThat(jsonContent).hasJsonPathValue("$.name", "John Doe");
        assertThat(jsonContent).hasJsonPathValue("$.email", "john.doe@example.com");
    }

    @Test
    void updateUserDtoSerialization() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto("Jane Doe", "jane.doe@example.com");

        JsonContent<UpdateUserDto> jsonContent = updateUserDtoJson.write(updateUserDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "Jane Doe");
        assertThat(jsonContent).hasJsonPathValue("$.email", "jane.doe@example.com");
    }

    @Test
    void createUserDtoSerialization() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Jack Smith", "jack.smith@example.com");

        JsonContent<CreateUserDto> jsonContent = createUserDtoJson.write(createUserDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).hasJsonPathValue("$.name", "Jack Smith");
        assertThat(jsonContent).hasJsonPathValue("$.email", "jack.smith@example.com");
    }
}
