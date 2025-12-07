package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private BookerDto booker;
    private ItemDto item;

    public static class BookerDto {
        private Long id;
        private String name;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BookerDto getBooker() { return booker; }
    public void setBooker(BookerDto booker) { this.booker = booker; }

    public ItemDto getItem() { return item; }
    public void setItem(ItemDto item) { this.item = item; }
}
