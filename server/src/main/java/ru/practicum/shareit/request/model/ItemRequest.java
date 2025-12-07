package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

public class ItemRequest {
    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }
}
