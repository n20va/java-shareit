package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest request, List<Item> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());

        List<ItemRequestDto.ItemResponseDto> itemsDto = (items == null) ? List.of() :
                items.stream()
                     .filter(Objects::nonNull)
                     .map(item -> {
                         ItemRequestDto.ItemResponseDto r = new ItemRequestDto.ItemResponseDto();
                         r.setId(item.getId());
                         r.setName(item.getName());
                         r.setOwnerId(item.getOwnerId());
                         return r;
                     })
                     .collect(Collectors.toList());

        dto.setItems(itemsDto);
        return dto;
    }

    public ItemRequest toItemRequest(CreateItemRequestDto dto, User requester) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
