package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.Collections;
import java.util.List;

public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository,
                           CommentRepository commentRepository,
                           ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwnerId(userId);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long userId, Item item) {
        Item existing = itemRepository.findById(item.getId());
        if (existing == null || !existing.getOwnerId().equals(userId)) {
            throw new RuntimeException("Нет доступа к изменению предмета");
        }
        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setAvailable(item.getAvailable());
        return itemRepository.save(existing);
    }

    @Override
    public Item getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId);
        return item;
    }

    @Override
    public List<Item> getItemsByUser(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text);
    }

    @Override
    public List<Item> getItemsByRequestId(Long requestId) {
        return itemRepository.findByRequestId(requestId);
    }
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            throw new RuntimeException("Item не найден");
        }
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(userId);
        return commentRepository.save(commentDto);
    }

    public List<CommentDto> getComments(Long itemId) {
        return commentRepository.findByItemId(itemId);
    }

}
