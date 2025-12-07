package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.BookingRequestDto;
import ru.practicum.shareit.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto dto, Long userId);

    BookingResponseDto approveBooking(Long bookingId, Long ownerId, boolean approved);

    BookingResponseDto getBooking(Long bookingId, Long userId);

    List<BookingResponseDto> getBookingsByUser(Long userId, String state, Integer from, Integer size);

    List<BookingResponseDto> getBookingsByOwner(Long ownerId, String state, Integer from, Integer size);

}
