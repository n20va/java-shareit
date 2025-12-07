package ru.practicum.shareit.service;


import ru.practicum.shareit.dto.CommentDto;

public interface CommentService {

    CommentDto addComment(Long userId, Long itemId, CommentDto dto);

}
