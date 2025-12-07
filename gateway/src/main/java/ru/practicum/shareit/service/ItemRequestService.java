package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.ItemRequestCreateDto;
import ru.practicum.shareit.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestCreateDto dto);

    List<ItemRequestDto> getOwn(Long userId);

    List<ItemRequestDto> getAll(Long userId, int from, int size);

    ItemRequestDto getById(Long userId, Long requestId);
}
