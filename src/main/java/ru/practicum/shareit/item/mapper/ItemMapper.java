package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return toItemDto(item, null, null, Collections.emptyList());
    }

    public static ItemDto toItemDto(Item item, ItemDto.BookingInfo lastBooking,
                                    ItemDto.BookingInfo nextBooking, List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        return toItemDto(item, null, null, comments);
    }

    public static Item toItemFromCreateDto(CreateItemDto createItemDto, Long ownerId) {
        if (createItemDto == null) {
            return null;
        }
        return new Item(
                null,
                createItemDto.getName(),
                createItemDto.getDescription(),
                createItemDto.getAvailable(),
                ownerId,
                createItemDto.getRequestId()
        );
    }

    public static Item updateItemFromDto(UpdateItemDto updateItemDto, Item item) {
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

    public static List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}