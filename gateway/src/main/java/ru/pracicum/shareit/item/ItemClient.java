package ru.pracicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pracicum.shareit.client.BaseClient;
import ru.pracicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.pracicum.shareit.item.model.item.dto.CreateItemDto;
import ru.pracicum.shareit.item.model.item.dto.UpdateItemDto;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, CreateItemDto createItemDto) {
        return post("", userId, createItemDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, Long userId, UpdateItemDto updateItemDto) {
        return patch("/%s".formatted(itemId), userId, updateItemDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/%s".formatted(itemId), userId);
    }

    public ResponseEntity<Object> getAllItemsByOwnerId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemsByText(String text, Long userId) {
        return get("/search?text=%s".formatted(text), userId);
    }

    public ResponseEntity<Object> createComment(CreateCommentDto createCommentDto, Long itemId, Long userId) {
        return post("/%s/comment".formatted(itemId), userId, createCommentDto);
    }
}
