package ru.pracicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pracicum.shareit.booking.model.TypeState;
import ru.pracicum.shareit.booking.model.dto.CreateBookingDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @ModelAttribute("userId")
    public Long getUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return userId;
    }


    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto,
                                        @ModelAttribute("userId") Long userId) {
        return bookingClient.createBooking(createBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@PathVariable Long bookingId,
                                          @RequestParam boolean approved,
                                          @ModelAttribute("userId") Long userId) {
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                 @ModelAttribute("userId") Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestParam(required = false, defaultValue = "ALL") TypeState state,
                                        @ModelAttribute("userId") Long userId) {
        return bookingClient.getBookings(state, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForOwner(@RequestParam(required = false, defaultValue = "ALL") TypeState state,
                                          @ModelAttribute("userId") Long userId) {
        return bookingClient.getBookingsForOwner(state, userId);
    }
}
