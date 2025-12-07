package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    private List<CommentDto> comments;

    @Getter
    @Setter
    @ToString
    public static class BookingInfo {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;

        public BookingInfo() {
        }

        public BookingInfo(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }

        public BookingInfo(Long id, Long bookerId, LocalDateTime start, LocalDateTime end) {
            this.id = id;
            this.bookerId = bookerId;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BookingInfo that = (BookingInfo) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(bookerId, that.bookerId) &&
                    Objects.equals(start, that.start) &&
                    Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, bookerId, start, end);
        }
    }

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public ItemDto(Long id, String name, String description, Boolean available) {
        this(id, name, description, available, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id)
                && Objects.equals(name, itemDto.name)
                && Objects.equals(description, itemDto.description)
                && Objects.equals(available, itemDto.available)
                && Objects.equals(requestId, itemDto.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, requestId);
    }
}