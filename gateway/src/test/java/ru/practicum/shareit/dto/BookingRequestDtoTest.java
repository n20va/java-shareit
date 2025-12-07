package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRequestDtoTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validateBookingRequestDto_withNullFields_returnsViolations() {
        BookingRequestDto dto = new BookingRequestDto();

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(3);
    }

    @Test
    void validateBookingRequestDto_withValidData_returnsNoViolations() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void validateBookingRequestDto_withStartInPast_returnsViolation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().minusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateBookingRequestDto_withEndInPast_returnsViolation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().minusDays(1));

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateBookingRequestDto_withStartEqualToEnd_returnsNoViolationForFutureAnnotation() {
        LocalDateTime sameTime = LocalDateTime.now().plusDays(1);
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(sameTime);
        dto.setEnd(sameTime);

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
