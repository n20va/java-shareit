package ru.practicum.shareit.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;

    private Long requestId;

    private Object lastBooking;
    private Object nextBooking;
    private List<CommentDto> comments;
}
