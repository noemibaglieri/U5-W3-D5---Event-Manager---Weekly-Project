package noemibaglieri.controllers;

import noemibaglieri.entities.Booking;
import noemibaglieri.entities.User;
import noemibaglieri.payloads.BookingDetailsDTO;
import noemibaglieri.payloads.NewBookingDTO;
import noemibaglieri.payloads.NewBookingRespDTO;
import noemibaglieri.services.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;

    @PostMapping
    public NewBookingRespDTO createBooking(@RequestBody @Validated NewBookingDTO payload,
                                           @AuthenticationPrincipal User currentUser) {
        Booking booking = bookingsService.bookEvent(payload, currentUser);
        return new NewBookingRespDTO(booking.getId());
    }

    @GetMapping("/me")
    public List<BookingDetailsDTO> getMyBookings(@AuthenticationPrincipal User currentUser) {
        return bookingsService.getBookingsByUser(currentUser)
                .stream()
                .map(b -> new BookingDetailsDTO(
                        b.getId(),
                        b.getEvent().getTitle(),
                        b.getEvent().getLocation(),
                        b.getEvent().getDate()
                ))
                .toList();
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public List<BookingDetailsDTO> getBookingsForEvent(@PathVariable long eventId,
                                                       @AuthenticationPrincipal User currentUser) {
        return bookingsService.getBookingsForEvent(eventId, currentUser)
                .stream()
                .map(b -> new BookingDetailsDTO(
                        b.getId(),
                        b.getEvent().getTitle(),
                        b.getEvent().getLocation(),
                        b.getEvent().getDate()
                ))
                .toList();
    }
}

