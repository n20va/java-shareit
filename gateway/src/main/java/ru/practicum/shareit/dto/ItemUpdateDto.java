package ru.practicum.shareit.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {

    private String name;
    private String description;
    private Boolean available;
}

