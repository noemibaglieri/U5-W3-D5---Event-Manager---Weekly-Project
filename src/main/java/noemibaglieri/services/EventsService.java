package noemibaglieri.services;

import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import noemibaglieri.exceptions.BadRequestException;
import noemibaglieri.exceptions.NotFoundException;
import noemibaglieri.payloads.NewEventDTO;
import noemibaglieri.payloads.UpdateEventDTO;
import noemibaglieri.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    public Event createEvent(NewEventDTO payload, User organiser) {
        if (!organiser.getRole().name().equals("EVENT_MANAGER") && !organiser.getRole().name().equals("ADMIN")) {
            throw new BadRequestException("Only event organisers or admins can create events");
        }

        if (payload.date().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Event date must be in the future");
        }

        boolean exists = eventsRepository.existsByTitleAndDateAndOrganiser(payload.title(), payload.date(), organiser);
        if (exists) {
            throw new BadRequestException("This event already exists");
        }

        Event newEvent = new Event(payload.title(), payload.description(), payload.location(), payload.date(), payload.availableSeats(), organiser);
        this.eventsRepository.save(newEvent);
        return newEvent;
    }

    public List<Event> getAvailableEvents() {
        return eventsRepository.findByAvailableSeatsGreaterThanAndDateAfter(0, LocalDateTime.now());
    }

    public List<Event> getEventsByOrganiser(User organiser) {
        return eventsRepository.findByOrganiser(organiser);
    }

    public Event updateEvent(long eventId, UpdateEventDTO payload, User requester) {
        Event existing = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        if (existing.getOrganiser().getId() != requester.getId()) {
            throw new BadRequestException("You can only edit your own events");
        }

        existing.setTitle(payload.title());
        existing.setDescription(payload.description().trim());
        existing.setLocation(payload.location().trim());
        existing.setDate(payload.date());
        existing.setAvailableSeats(payload.availableSeats());

        return eventsRepository.save(existing);
    }


    public void deleteEvent(long eventId, User requester) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getOrganiser().getId() != requester.getId()) {
            throw new BadRequestException("You can only delete your own events");
        }

        eventsRepository.delete(event);
    }

    public Event getById(long id) {
        return eventsRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
    }
}

