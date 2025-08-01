package noemibaglieri.services;

import noemibaglieri.entities.Booking;
import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import noemibaglieri.exceptions.BadRequestException;
import noemibaglieri.exceptions.NotFoundException;
import noemibaglieri.exceptions.UnauthorisedException;
import noemibaglieri.payloads.NewBookingDTO;
import noemibaglieri.repositories.BookingsRepository;
import noemibaglieri.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingsService {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    BookingsRepository bookingsRepository;

    public Booking bookEvent(NewBookingDTO payload, User user) {

        Event event = eventsRepository.findById(payload.eventId()).orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book past events");
        }

        if (event.getAvailableSeats() <= 0) {
            throw new BadRequestException("No available seats");
        }

        boolean alreadyBooked = bookingsRepository.existsByUserAndEvent(user, event);
        if (alreadyBooked) {
            throw new BadRequestException("You already booked this event");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventsRepository.save(event);

        Booking booking = new Booking(user, event);
        return bookingsRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingsRepository.findByUser(user);
    }

    public List<Booking> getBookingsForEvent(long eventId, User requester) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        boolean isOwner = event.getOrganiser().getId() == requester.getId();
        boolean isAdminOrEventManager = requester.getRole().name().equals("ADMIN") || requester.getRole().name().equals("EVENT_MANAGER");

        if (!isOwner && !isAdminOrEventManager) {
            throw new UnauthorisedException("You can't view bookings for this event");
        }

        return bookingsRepository.findByEvent(event);
    }


}
