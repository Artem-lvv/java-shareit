package ru.pracicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pracicum.shareit.client.BaseClient;
import ru.pracicum.shareit.user.model.dto.CreateUserDto;
import ru.pracicum.shareit.user.model.dto.UpdateUserDto;

@Component
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(CreateUserDto createUserDto) {
        return post("", createUserDto);
    }

    public ResponseEntity<Object> updateUser(Long userId, UpdateUserDto updateUserDto) {
        return patch("/%s".formatted(userId), updateUserDto);
    }

    public void deleteUser(long id) {
        delete("/%s".formatted(id));
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return get("/%s".formatted(id));
    }
}
