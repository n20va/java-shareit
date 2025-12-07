package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public ItemDto toItemDto(Item item, List<CommentDto> comments) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        dto.setComments(comments);
        return dto;
    }

    public Item toItem(CreateItemDto dto) {
        if (dto == null) return null;
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public void updateItemFromDto(UpdateItemDto dto, Item item) {
        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getAvailable() != null) item.setAvailable(dto.getAvailable());
    }

    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) return null;
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null);
        dto.setItemId(comment.getItem() != null ? comment.getItem().getId() : null);
        return dto;
    }

    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        if (comments == null) return List.of();
        return comments.stream().map(this::toCommentDto).collect(Collectors.toList());
    }
}
