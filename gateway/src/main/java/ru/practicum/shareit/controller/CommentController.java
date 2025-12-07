package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {

    private final CommentService service;

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentDto dto) {
        return service.addComment(userId, itemId, dto);
    }
}