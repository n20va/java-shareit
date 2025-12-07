package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeCommentDto_correctly() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        CommentDto dto = new CommentDto();
        dto.setText("Хорошая вещь");
        dto.setAuthorName("Автор");
        dto.setCreated(null);

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("Хорошая вещь");

        assertThat(json).contains("authorName");
        assertThat(json).contains("created");
    }

    @Test
    void deserializeFields_correctly() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        String json = "{"
                + "\"id\": 99,"
                + "\"text\": \"Комментарий\","
                + "\"authorName\": \"Hacker\","
                + "\"created\": \"2025-01-01T12:00:00\""
                + "}";

        CommentDto dto = objectMapper.readValue(json, CommentDto.class);

        assertThat(dto.getText()).isEqualTo("Комментарий");
        assertThat(dto.getId()).isNull();
        assertThat(dto.getAuthorName()).isEqualTo("Hacker");
        assertThat(dto.getCreated()).isNotNull();
    }
}
