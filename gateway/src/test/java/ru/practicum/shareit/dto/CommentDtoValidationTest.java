package ru.practicum.shareit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void whenTextIsNull_thenValidationFails() {
        CommentDto dto = new CommentDto();
        dto.setText(null);

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void whenTextIsBlank_thenValidationFails() {
        CommentDto dto = new CommentDto();
        dto.setText("   ");

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void whenTextIsValid_thenValidationPasses() {
        CommentDto dto = new CommentDto();
        dto.setText("Отличная вещь!");

        Set<ConstraintViolation<CommentDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
