package ru.practicum.shareit.item.model;

import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String text;
    private Item item;
    private User author;
    private LocalDateTime created;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }
}
