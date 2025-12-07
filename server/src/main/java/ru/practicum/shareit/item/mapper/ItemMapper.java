package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item toItem(CreateItemDto dto) {
        if (dto == null) return null;
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setRequestId(dto.getRequestId());
        return item;
    }

    public static Item toItem(UpdateItemDto dto, Item existingItem) {
        if (dto.getName() != null) existingItem.setName(dto.getName());
        if (dto.getDescription() != null) existingItem.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) existingItem.setAvailable(dto.getAvailable());
        return existingItem;
    }

    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        return dto;
    }
}
