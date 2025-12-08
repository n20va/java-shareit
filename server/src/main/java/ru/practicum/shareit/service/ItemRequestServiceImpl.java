package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.entity.Item;
import ru.practicum.shareit.entity.ItemRequest;
import ru.practicum.shareit.entity.User;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.ItemRequestRepository;
import ru.practicum.shareit.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestCreateDto dto) {
        User user = getUserOrThrow(userId);

        ItemRequest request = mapper.toEntity(dto, user);
        ItemRequest saved = repository.save(request);

        return mapper.toResponseDto(saved, List.of());
    }

    @Override
    public List<ItemRequestDto> getOwn(Long userId) {
        getUserOrThrow(userId);

        List<ItemRequest> requests = repository.findAllByRequesterIdOrderByCreatedDesc(userId);
        if (requests.isEmpty()) {
            return List.of();
        }

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<Item>> itemsByRequestId = loadItemsByRequestIds(requestIds);

        return requests.stream()
                .map(r -> {
                    List<Item> items = itemsByRequestId.getOrDefault(r.getId(), List.of());
                    return mapper.toResponseDto(r, items);
                })
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAll(Long userId, int from, int size) {
        getUserOrThrow(userId);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        Page<ItemRequest> page =
                repository.findAllByRequesterIdNot(userId, pageable);

        List<ItemRequest> requests = page.getContent();
        if (requests.isEmpty()) {
            return List.of();
        }

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<Item>> itemsByRequestId = loadItemsByRequestIds(requestIds);

        return requests.stream()
                .map(r -> {
                    List<Item> items = itemsByRequestId.getOrDefault(r.getId(), List.of());
                    return mapper.toResponseDto(r, items);
                })
                .toList();
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) {
        getUserOrThrow(userId);

        ItemRequest request = repository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));

        Map<Long, List<Item>> itemsByRequestId = loadItemsByRequestIds(List.of(request.getId()));
        List<Item> items = itemsByRequestId.getOrDefault(request.getId(), List.of());

        return mapper.toResponseDto(request, items);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    private Map<Long, List<Item>> loadItemsByRequestIds(List<Long> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) {
            return Map.of();
        }
        List<Item> items = itemRepository.findAllByRequestIdIn(requestIds);
        return items.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));
    }
}
