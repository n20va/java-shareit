package ru.practicum.shareit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String authorName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
}
