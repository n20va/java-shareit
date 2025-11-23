package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currentId = 1L;

    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(currentId++);
        }
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> containsText(item, searchText))
                .collect(Collectors.toList());
    }

    private boolean containsText(Item item, String searchText) {
        return (item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText));
    }

    public void deleteById(Long id) {
        items.remove(id);
    }

    public boolean existsById(Long id) {
        return items.containsKey(id);
    }

    public boolean isOwner(Long itemId, Long userId) {
        return findById(itemId)
                .map(item -> item.getOwnerId().equals(userId))
                .orElse(false);
    }

    public List<Item> findByRequestId(Long requestId) {
        return items.values().stream()
                .filter(item -> requestId.equals(item.getRequestId()))
                .collect(Collectors.toList());
    }

    public void clear() {
        items.clear();
        currentId = 1L;
    }

    public int count() {
        return items.size();
    }
}