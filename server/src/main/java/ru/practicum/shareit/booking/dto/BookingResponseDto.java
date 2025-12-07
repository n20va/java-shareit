package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.status.BookingStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private BookerDto booker;
    private ItemDto item;

    @Getter
    @Setter
    @ToString
    public static class BookerDto {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @ToString
    public static class ItemDto {
        private Long id;
        private String name;
    }
}