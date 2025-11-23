package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
    private String name;
    private String description;
    private Boolean available;

    public UpdateItemDto() {
    }

    public UpdateItemDto(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}