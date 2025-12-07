package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public final class ItemMapper {

    private ItemMapper() { }

    public static Item toItemFromCreateDto(CreateItemDto dto, Long ownerId) {
        return new Item(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                ownerId,
                dto.getRequestId()
        );
    }

    public static void updateItemFromDto(UpdateItemDto dto, Item item) {
        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) item.setAvailable(dto.getAvailable());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                List.of()
        );
    }

    public static ItemDto toItemDto(Item item,
                                    ItemDto.BookingInfo lastBooking,
                                    ItemDto.BookingInfo nextBooking,
                                    List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static ItemDto toItemDtoWithComments(Item item, List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                comments
        );
    }
}
