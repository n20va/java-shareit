package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Long ownerId);

    ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long ownerId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getUserItems(Long ownerId);

    List<ItemDto> searchItems(String text);

    void deleteItem(Long itemId, Long ownerId);
}