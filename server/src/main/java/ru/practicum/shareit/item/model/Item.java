package ru.practicum.shareit.item.model;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.model.ItemRequest;

public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public Long getOwnerId() { return owner != null ? owner.getId() : null; }

    public ItemRequest getRequest() { return request; }
    public void setRequest(ItemRequest request) { this.request = request; }
    public Long getRequestId() { return request != null ? request.getId() : null; }
}
