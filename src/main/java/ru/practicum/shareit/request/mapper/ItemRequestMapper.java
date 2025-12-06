package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequesterId(request.getRequester().getId());
        dto.setCreated(request.getCreated());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            dto.setItems(request.getItems().stream()
                    .map(ItemRequestMapper::toItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private static ItemRequestDto.ItemResponseDto toItemResponseDto(Item item) {
        ItemRequestDto.ItemResponseDto dto = new ItemRequestDto.ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        dto.setOwnerId(item.getOwnerId());
        return dto;
    }
}