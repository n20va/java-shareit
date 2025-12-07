package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.comment.CommentDto;
import ru.practicum.shareit.service.CommentService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/items/{itemId}/comment")
public CommentDto addComment(
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @PathVariable Long itemId,
        @RequestBody CommentDto dto) {
    return commentService.addComment(userId, itemId, dto);
}


    @GetMapping("/{itemId}/comments")
    public List<CommentDto> getCommentsByItem(
            @PathVariable Long itemId) {
        return commentService.getCommentsByItem(itemId);
    }
}
