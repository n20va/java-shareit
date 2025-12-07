package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build(),
                serverUrl + API_PREFIX
        );
    }

    public ResponseEntity<Object> createBooking(BookingDto bookingDto, Long bookerId) {
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long bookingId, Boolean approved, Long ownerId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(Long bookingId, Long userId) {
        Map<String, Object> parameters = Map.of("bookingId", bookingId);
        return get("/{bookingId}", userId, parameters);
    }

    public ResponseEntity<Object> getUserBookings(BookingState state, Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.toString(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookings(BookingState state, Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.toString(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }
}