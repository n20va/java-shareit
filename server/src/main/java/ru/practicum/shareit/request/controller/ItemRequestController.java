package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(
            @RequestBody CreateItemRequestDto createItemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        return requestService.createRequest(createItemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        return requestService.getUserRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return requestService.getRequestById(requestId, userId);
    }
}

