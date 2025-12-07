package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.ItemCreateDto;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.dto.ItemUpdateDto;
import ru.practicum.shareit.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @Valid @RequestBody ItemCreateDto dto) {
        return itemService.createItem(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long itemId,
            @RequestBody ItemUpdateDto dto) {
        return itemService.updateItem(itemId, dto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

}
