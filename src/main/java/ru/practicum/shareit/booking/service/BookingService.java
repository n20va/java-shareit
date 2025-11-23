package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingDto bookingDto, Long bookerId);

    BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long ownerId);

    BookingResponseDto getBookingById(Long bookingId, Long userId);

    List<BookingResponseDto> getUserBookings(BookingState state, Long userId, int from, int size);

    List<BookingResponseDto> getOwnerBookings(BookingState state, Long ownerId, int from, int size);
}