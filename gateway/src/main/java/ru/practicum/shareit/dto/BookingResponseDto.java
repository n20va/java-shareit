package ru.practicum.shareit.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private ItemDto item;

    private UserDto booker;
}