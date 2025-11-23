package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
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