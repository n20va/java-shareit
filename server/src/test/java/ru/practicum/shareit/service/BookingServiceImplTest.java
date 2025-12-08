package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.booking.BookingRequestDto;
import ru.practicum.shareit.dto.booking.BookingResponseDto;
import ru.practicum.shareit.entity.Booking;
import ru.practicum.shareit.entity.BookingStatus;
import ru.practicum.shareit.entity.Item;
import ru.practicum.shareit.entity.User;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldCreateBookingSuccessfully() {
        User owner = createUser("owner@test.com", "Owner");
        User booker = createUser("booker@test.com", "Booker");
        Item item = createItem(owner, "Test Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());

        assertNotNull(booking);
        assertEquals(item.getId(), booking.getItem().getId());
        assertEquals(booker.getId(), booking.getBooker().getId());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void shouldApproveBookingSuccessfully() {
        User owner = createUser("owner2@test.com", "Owner2");
        User booker = createUser("booker2@test.com", "Booker2");
        Item item = createItem(owner, "Test Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());
        BookingResponseDto approved = bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertEquals(BookingStatus.APPROVED, approved.getStatus());
    }

    @Test
    void createBookingFailsWhenItemNotAvailable() {
        User owner = createUser("owner3@test.com", "Owner3");
        User booker = createUser("booker3@test.com", "Booker3");
        Item item = createItem(owner, "Test Item", false);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(dto, booker.getId()));
    }

    @Test
    void createBookingFailsOwnItem() {
        User owner = createUser("owner4@test.com", "Owner4");
        Item item = createItem(owner, "Own Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(dto, owner.getId()));
    }

    @Test
    void approveBookingFailsWhenNotOwner() {
        User owner = createUser("owner5@test.com", "Owner5");
        User other = createUser("other@test.com", "Other");
        User booker = createUser("booker4@test.com", "Booker4");
        Item item = createItem(owner, "Test Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());

        assertThrows(ru.practicum.shareit.exception.ForbiddenException.class,
                () -> bookingService.approveBooking(booking.getId(), other.getId(), true));
    }

    @Test
    void approveBookingFailsWhenNotWaiting() {
        User owner = createUser("owner6@test.com", "Owner6");
        User booker = createUser("booker5@test.com", "Booker5");
        Item item = createItem(owner, "Test Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());
        bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.approveBooking(booking.getId(), owner.getId(), false));
    }

    @Test
    void getBookingFailsWhenNoAccess() {
        User owner = createUser("owner7@test.com", "Owner7");
        User booker = createUser("booker6@test.com", "Booker6");
        User stranger = createUser("stranger@test.com", "Stranger");
        Item item = createItem(owner, "Test Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());

        assertThrows(ru.practicum.shareit.exception.ForbiddenException.class,
                () -> bookingService.getBooking(booking.getId(), stranger.getId()));
    }

    @Test
    void getBookingNotFound() {
        assertThrows(ru.practicum.shareit.exception.NotFoundException.class,
                () -> bookingService.getBooking(999L, 1L));
    }

    @Test
    void approveBookingNotFound() {
        assertThrows(ru.practicum.shareit.exception.NotFoundException.class,
                () -> bookingService.approveBooking(999L, 1L, true));
    }

    @Test
    void getBookingsByUserAllState() {
        User owner = createUser("owner8@test.com", "Owner8");
        User booker = createUser("booker7@test.com", "Booker7");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(dto, booker.getId());

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByUser(booker.getId(), "ALL", 0, 10);

        assertEquals(1, bookings.size());
    }

    @Test
    void getBookingsByUserFutureState() {
        User owner = createUser("owner9@test.com", "Owner9");
        User booker = createUser("booker8@test.com", "Booker8");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(2));
        dto.setEnd(LocalDateTime.now().plusDays(3));
        bookingService.createBooking(dto, booker.getId());

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByUser(booker.getId(), "FUTURE", 0, 10);

        assertEquals(1, bookings.size());
    }

    @Test
    void getBookingsByUserCurrentState() {
        User owner = createUser("owner10@test.com", "Owner10");
        User booker = createUser("booker9@test.com", "Booker9");
        Item item = createItem(owner, "Item", true);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByUser(booker.getId(), "CURRENT", 0, 10);

        assertEquals(1, bookings.size());
    }

    @Test
    void getBookingsByUserWaitingState() {
        User owner = createUser("owner11@test.com", "Owner11");
        User booker = createUser("booker10@test.com", "Booker10");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(dto, booker.getId());

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByUser(booker.getId(), "WAITING", 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
    }

    @Test
    void getBookingsByUserRejectedState() {
        User owner = createUser("owner12@test.com", "Owner12");
        User booker = createUser("booker11@test.com", "Booker11");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        BookingResponseDto booking = bookingService.createBooking(dto, booker.getId());

        bookingService.approveBooking(booking.getId(), owner.getId(), false);

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByUser(booker.getId(), "REJECTED", 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.REJECTED, bookings.get(0).getStatus());
    }

    @Test
    void getBookingsByUserUnknownState() {
        User booker = createUser("booker12@test.com", "Booker12");

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookingsByUser(booker.getId(), "UNKNOWN", 0, 10));
    }

    @Test
    void getBookingsByUserWithPagination() {
        User owner = createUser("owner13@test.com", "Owner13");
        User booker = createUser("booker13@test.com", "Booker13");
        Item item = createItem(owner, "Item", true);

        for (int i = 1; i <= 3; i++) {
            BookingRequestDto dto = new BookingRequestDto();
            dto.setItemId(item.getId());
            dto.setStart(LocalDateTime.now().plusDays(i));
            dto.setEnd(LocalDateTime.now().plusDays(i + 1));
            bookingService.createBooking(dto, booker.getId());
        }

        List<BookingResponseDto> page =
                bookingService.getBookingsByUser(booker.getId(), "ALL", 1, 1);

        assertEquals(1, page.size());
    }

    @Test
    void findLastBookingReturnsNullWhenNoBookings() {
        Item item = createItem(createUser("owner14@test.com", "Owner14"), "Item", true);
        Booking last = ((BookingServiceImpl) bookingService).findLastBooking(item.getId());
        assertNull(last);
    }

    @Test
    void findNextBookingReturnsNullWhenNoBookings() {
        Item item = createItem(createUser("owner15@test.com", "Owner15"), "Item", true);
        Booking next = ((BookingServiceImpl) bookingService).findNextBooking(item.getId());
        assertNull(next);
    }

    @Test
    void getBookingsByOwnerAllState() {
        User owner = createUser("owner16@test.com", "Owner16");
        User booker = createUser("booker14@test.com", "Booker14");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(dto, booker.getId());

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByOwner(owner.getId(), "ALL", 0, 10);

        assertEquals(1, bookings.size());
    }

    @Test
    void getBookingsByOwnerWaitingState() {
        User owner = createUser("owner17@test.com", "Owner17");
        User booker = createUser("booker15@test.com", "Booker15");
        Item item = createItem(owner, "Item", true);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(dto, booker.getId());

        List<BookingResponseDto> bookings =
                bookingService.getBookingsByOwner(owner.getId(), "WAITING", 0, 10);

        assertEquals(1, bookings.size());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
    }

    private User createUser(String email, String name) {
        User u = new User();
        u.setEmail(email);
        u.setName(name);
        return userRepository.save(u);
    }

    private Item createItem(User owner, String name, boolean available) {
        Item i = new Item();
        i.setName(name);
        i.setDescription("Description");
        i.setAvailable(available);
        i.setOwner(owner);
        return itemRepository.save(i);
    }
}
