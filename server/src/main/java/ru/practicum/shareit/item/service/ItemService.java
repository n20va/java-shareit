package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Item item);

    Item getItemById(Long itemId, Long userId);

    List<Item> getItemsByUser(Long userId);

    List<Item> searchItems(String text);

    List<Item> getItemsByRequestId(Long requestId);
}
