package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public final class ItemRequestMapper {

    private ItemRequestMapper() { }

    public static ItemRequestDto toItemRequestDto(ItemRequest request, ItemService itemService) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequesterId(request.getRequester().getId());
        dto.setCreated(request.getCreated());

        List<ItemDto> items = itemService.getItemsByRequestId(request.getId());

        if (items != null && !items.isEmpty()) {
            dto.setItems(items.stream()
                    .map(ItemRequestMapper::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private static ItemRequestDto.ItemResponseDto toItemResponseDto(ItemDto itemDto) {
        ItemRequestDto.ItemResponseDto dto = new ItemRequestDto.ItemResponseDto();
        dto.setId(itemDto.getId());
        dto.setName(itemDto.getName());
        dto.setDescription(itemDto.getDescription());
        dto.setAvailable(itemDto.getAvailable());
        dto.setRequestId(itemDto.getRequestId());
        return dto;
    }
}
