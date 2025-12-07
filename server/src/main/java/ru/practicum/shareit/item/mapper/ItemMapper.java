package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public Item toItemFromCreateDto(CreateItemDto dto, Long ownerId) {
        return new Item(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                ownerId,
                dto.getRequestId()
        );
    }

    public void updateItemFromDto(UpdateItemDto dto, Item item) {
        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) item.setAvailable(dto.getAvailable());
    }

    public ItemDto toItemDto(Item item) {
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

    public ItemDto toItemDto(Item item,
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

    public ItemDto toItemDtoWithComments(Item item, List<CommentDto> comments) {
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
