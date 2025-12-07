package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto createItem(Long ownerId, CreateItemDto dto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = ItemMapper.toItemFromCreateDto(dto, ownerId);
        item.setOwner(owner);

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem, List.of());
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, UpdateItemDto dto) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!existingItem.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("You are not the owner of this item");
        }

        ItemMapper.updateItemFromDto(dto, existingItem);
        Item savedItem = itemRepository.save(existingItem);

        List<CommentDto> comments = commentRepository.findByItemId(savedItem.getId())
                .stream().map(comment -> {
                    CommentDto c = new CommentDto();
                    c.setId(comment.getId());
                    c.setText(comment.getText());
                    c.setAuthorName(comment.getAuthor().getName());
                    c.setCreated(comment.getCreated());
                    return c;
                }).collect(Collectors.toList());

        return ItemMapper.toItemDto(savedItem, comments);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                .stream().map(comment -> {
                    CommentDto c = new CommentDto();
                    c.setId(comment.getId());
                    c.setText(comment.getText());
                    c.setAuthorName(comment.getAuthor().getName());
                    c.setCreated(comment.getCreated());
                    return c;
                }).collect(Collectors.toList());

        return ItemMapper.toItemDto(item, comments);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);

        return items.stream().map(item -> {
            List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                    .stream().map(comment -> {
                        CommentDto c = new CommentDto();
                        c.setId(comment.getId());
                        c.setText(comment.getText());
                        c.setAuthorName(comment.getAuthor().getName());
                        c.setCreated(comment.getCreated());
                        return c;
                    }).collect(Collectors.toList());

            return ItemMapper.toItemDto(item, comments);
        }).collect(Collectors.toList());
    }
}
