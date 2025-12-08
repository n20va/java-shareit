package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.ItemCreateDto;
import ru.practicum.shareit.dto.ItemDto;
import ru.practicum.shareit.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long ownerId, ItemCreateDto dto);

    ItemDto updateItem(Long itemId, ItemUpdateDto dto, Long ownerId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> search(String text);
}
