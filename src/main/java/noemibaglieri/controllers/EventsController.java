package noemibaglieri.controllers;

import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import noemibaglieri.payloads.NewEventDTO;
import noemibaglieri.payloads.UpdateEventDTO;
import noemibaglieri.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsService eventService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('EVENT_MANAGER', 'ADMIN')")
    public Event create(@RequestBody @Validated NewEventDTO payload) {
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
    public Event update(@PathVariable long id, @RequestBody @Validated UpdateEventDTO payload) {
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
