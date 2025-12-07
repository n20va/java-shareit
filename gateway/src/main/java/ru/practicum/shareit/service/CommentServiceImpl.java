package ru.practicum.shareit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.client.CommentClient;
import ru.practicum.shareit.dto.CommentDto;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentClient commentClient;
    private final ObjectMapper mapper;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto dto) {
        var response = commentClient.createComment(userId, itemId, dto);

        if (response.getStatusCode().is2xxSuccessful()) {
            return mapper.convertValue(response.getBody(), CommentDto.class);
        }

        throw new ResponseStatusException(
                response.getStatusCode(),
                response.getBody() != null ? response.getBody().toString() : null
        );
    }
}
