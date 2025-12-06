package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long itemId, CreateCommentDto createCommentDto, Long authorId);

    List<CommentDto> getCommentsByItemId(Long itemId);

    List<CommentDto> getCommentsByItemIds(List<Long> itemIds);
}