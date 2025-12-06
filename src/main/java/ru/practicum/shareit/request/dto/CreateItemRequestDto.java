package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateItemRequestDto {
    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
}