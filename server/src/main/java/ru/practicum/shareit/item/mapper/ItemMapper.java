package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return toItemDto(item, null, null, Collections.emptyList());
    }

    public ItemDto toItemDto(Item item, ItemDto.BookingInfo lastBooking,
                             ItemDto.BookingInfo nextBooking, List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequestId());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(comments != null ? comments : new ArrayList<>());
        return itemDto;
    }

    public Item toItemFromCreateDto(CreateItemDto createItemDto, Long ownerId) {
        if (createItemDto == null) {
            return null;
        }
        Item item = new Item();
        item.setName(createItemDto.getName());
        item.setDescription(createItemDto.getDescription());
        item.setAvailable(createItemDto.getAvailable());
        item.setOwnerId(ownerId);
        item.setRequestId(createItemDto.getRequestId());
        return item;
    }

    public Item updateItemFromDto(UpdateItemDto updateItemDto, Item item) {
        if (updateItemDto == null || item == null) {
            return item;
        }

        if (updateItemDto.getName() != null && !updateItemDto.getName().isBlank()) {
            item.setName(updateItemDto.getName());
        }

        if (updateItemDto.getDescription() != null && !updateItemDto.getDescription().isBlank()) {
            item.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        return item;
    }
}
