package ru.pracicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pracicum.shareit.booking.model.TypeState;
import ru.pracicum.shareit.booking.model.dto.CreateBookingDto;
import ru.pracicum.shareit.client.BaseClient;

@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(CreateBookingDto createBookingDto, Long userId) {
        return post("", userId, createBookingDto);
    }

    public ResponseEntity<Object> updateBookingStatus(Long bookingId, boolean approved, Long userId) {
        return patch("/%s?approved=%s".formatted(bookingId, approved), userId, null, null);
    }

    public ResponseEntity<Object> getBooking(Long bookingId, Long userId) {
        return get("/%s".formatted(bookingId), userId);
    }

    public ResponseEntity<Object> getBookings(TypeState state, Long userId) {
        return get("?state=%s".formatted(state), userId);
    }

    public ResponseEntity<Object> getBookingsForOwner(TypeState state, Long userId) {
        return get("/owner?state=%s".formatted(state), userId);
    }
}
