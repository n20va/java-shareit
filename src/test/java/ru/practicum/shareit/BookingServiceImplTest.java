package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        owner = new User(1L, "Owner", "owner@example.com");
        booker = new User(2L, "Booker", "booker@example.com");
        item = new Item(1L, "Дрель", "Мощная дрель", true, owner.getId(), null);
        booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, booker, BookingStatus.WAITING);
        bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking_WithValidData_ShouldReturnBookingResponseDto() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto result = bookingService.createBooking(bookingDto, booker.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_WhenUserIsOwner_ShouldThrowNotFoundException() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, owner.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_WhenItemNotAvailable_ShouldThrowValidationException() {
        item.setAvailable(false);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, booker.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_WithInvalidDates_ShouldThrowValidationException() {
        bookingDto.setStart(LocalDateTime.now().plusDays(3));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, booker.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_WithValidData_ShouldReturnApprovedBooking() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto result = bookingService.approveBooking(booking.getId(), true, owner.getId());

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void approveBooking_WhenUserNotOwner_ShouldThrowNotFoundException() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(booking.getId(), true, booker.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_WhenAlreadyApproved_ShouldThrowValidationException() {
        booking.setStatus(BookingStatus.APPROVED);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(booking.getId(), true, owner.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingById_WhenUserIsOwner_ShouldReturnBooking() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.getBookingById(booking.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingById_WhenUserIsBooker_ShouldReturnBooking() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingResponseDto result = bookingService.getBookingById(booking.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingById_WhenUserNotOwnerOrBooker_ShouldThrowNotFoundException() {
        User otherUser = new User(3L, "Other", "other@example.com");
        when(userRepository.findById(otherUser.getId())).thenReturn(Optional.of(otherUser));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(), otherUser.getId()));
    }

    @Test
    void getUserBookings_WithAllState_ShouldReturnBookings() {
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdOrderByStartDesc(eq(booker.getId()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> result = bookingService.getUserBookings(
                BookingState.ALL, booker.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getOwnerBookings_WithAllState_ShouldReturnBookings() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(eq(owner.getId()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> result = bookingService.getOwnerBookings(
                BookingState.ALL, owner.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }
}

