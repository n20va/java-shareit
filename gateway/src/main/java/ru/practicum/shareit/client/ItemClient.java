package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build(),
                serverUrl + API_PREFIX
        );
    }

    public ResponseEntity<Object> createItem(CreateItemDto createItemDto, Long ownerId) {
        return post("", ownerId, createItemDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, UpdateItemDto updateItemDto, Long ownerId) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        return patch("/{itemId}", ownerId, parameters, updateItemDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        if (userId != null) {
            return get("/{itemId}", userId, parameters);
        } else {
            return get("/{itemId}", parameters);
        }
    }

    public ResponseEntity<Object> getUserItems(Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("", ownerId, parameters);
    }

    public ResponseEntity<Object> searchItems(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search", null, parameters);
    }

    public ResponseEntity<Object> deleteItem(Long itemId, Long ownerId) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        return delete("/{itemId}", ownerId, parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, CreateCommentDto createCommentDto, Long userId) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        return post("/{itemId}/comment", userId, parameters, createCommentDto);
    }
}