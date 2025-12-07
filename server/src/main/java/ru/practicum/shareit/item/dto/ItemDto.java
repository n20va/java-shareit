package ru.practicum.shareit.item.dto;

import java.util.List;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    private List<CommentDto> comments;

    public ItemDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public BookingInfo getLastBooking() { return lastBooking; }
    public void setLastBooking(BookingInfo lastBooking) { this.lastBooking = lastBooking; }

    public BookingInfo getNextBooking() { return nextBooking; }
    public void setNextBooking(BookingInfo nextBooking) { this.nextBooking = nextBooking; }

    public List<CommentDto> getComments() { return comments; }
    public void setComments(List<CommentDto> comments) { this.comments = comments; }

    public static class BookingInfo {
        private Long id;
        private Long bookerId;

        public BookingInfo() {}
        public BookingInfo(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getBookerId() { return bookerId; }
        public void setBookerId(Long bookerId) { this.bookerId = bookerId; }
    }
}
