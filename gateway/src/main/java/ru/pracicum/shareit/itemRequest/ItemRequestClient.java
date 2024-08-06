package ru.pracicum.shareit.itemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pracicum.shareit.client.BaseClient;
import ru.pracicum.shareit.itemRequest.model.dto.CreateItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> createItemRequest(CreateItemRequestDto createItemRequestDto, Long userId) {
        return post("", userId, createItemRequestDto);
    }

    public ResponseEntity<Object> getRequestsByUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequestsById(Long requestId, Long userId) {
        return get("/%s".formatted(requestId), userId);
    }

    public ResponseEntity<Object> getAllByPage(Integer from, Integer size) {
        return get("/all?from=%s&size=%s".formatted(from, size));
    }
}
