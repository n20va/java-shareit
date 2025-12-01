package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    @NotNull
    private Long itemId;
    
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    
    @NotNull
    @Future
    private LocalDateTime end;
}
