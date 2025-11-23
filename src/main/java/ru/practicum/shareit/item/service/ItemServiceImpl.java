package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto createItem(CreateItemDto createItemDto, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item item = ItemMapper.toItemFromCreateDto(createItemDto, ownerId);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item existingItem = getItemByIdOrThrow(itemId);

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        Item updatedItem = ItemMapper.updateItemFromDto(updateItemDto, existingItem);
        Item savedItem = itemRepository.update(updatedItem);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = getItemByIdOrThrow(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        userService.getUserEntity(ownerId);

        return itemRepository.findByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item item = getItemByIdOrThrow(itemId);

        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        itemRepository.deleteById(itemId);
    }

    private Item getItemByIdOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));
    }
}