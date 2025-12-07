package ru.practicum.shareit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(message = "ID вещи обязательно")
    private Long itemId;

    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime end;
}

