package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(CreateItemRequestDto createItemRequestDto, Long requesterId);

    ItemRequestDto getRequestById(Long requestId, Long userId);

    List<ItemRequestDto> getUserRequests(Long requesterId);

    List<ItemRequestDto> getAllRequests(Long userId, int from, int size);
}