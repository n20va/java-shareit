package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @RequestBody CreateItemDto createItemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.createItem(createItemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @PathVariable Long itemId,
            @RequestBody UpdateItemDto updateItemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.updateItem(itemId, updateItemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam String text
    ) {
        return itemService.searchItems(text);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        itemService.deleteItem(itemId, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @PathVariable Long itemId,
            @RequestBody CreateCommentDto createCommentDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemService.addComment(itemId, createCommentDto, userId);
    }

}
