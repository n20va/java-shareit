package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Long ownerId);

    ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long ownerId);

    ItemDto getItemById(Long itemId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getUserItems(Long ownerId);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long itemId, Long ownerId);

    CommentDto addComment(Long itemId, CreateCommentDto createCommentDto, Long authorId);

    List<Item> getItemsByRequestId(Long requestId);


}
