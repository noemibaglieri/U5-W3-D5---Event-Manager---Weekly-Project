package noemibaglieri.controllers;

import jakarta.validation.Valid;
import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import noemibaglieri.payloads.NewEventDTO;
import noemibaglieri.payloads.UpdateEventDTO;
import noemibaglieri.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventsService eventService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public Event create(@RequestBody @Valid NewEventDTO payload) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventService.createEvent(payload, currentUser);
    }

    @GetMapping("/available")
    public List<Event> getAvailableEvents() {
        return eventService.getAvailableEvents();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public List<Event> getMyEvents() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventService.getEventsByOrganiser(currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public Event update(@PathVariable long id, @RequestBody @Valid UpdateEventDTO payload) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventService.updateEvent(id, payload, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public void delete(@PathVariable long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        eventService.deleteEvent(id, currentUser);
    }
}
