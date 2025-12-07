package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public User getBooker() { return booker; }
    public void setBooker(User booker) { this.booker = booker; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
