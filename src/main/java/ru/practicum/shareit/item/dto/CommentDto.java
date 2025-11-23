package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
    private Long itemId;

    public CommentDto() {
    }

    public CommentDto(Long id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }

    public CommentDto(Long id, String text, String authorName, LocalDateTime created, Long itemId) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
        this.itemId = itemId;
    }
}