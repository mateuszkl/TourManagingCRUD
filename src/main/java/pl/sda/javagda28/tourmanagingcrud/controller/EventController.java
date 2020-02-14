package pl.sda.javagda28.tourmanagingcrud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.sda.javagda28.tourmanagingcrud.entity.Band;
import pl.sda.javagda28.tourmanagingcrud.entity.Event;

import pl.sda.javagda28.tourmanagingcrud.entity.Venue;
import pl.sda.javagda28.tourmanagingcrud.dto.EventForm;
import pl.sda.javagda28.tourmanagingcrud.service.BandService;
import pl.sda.javagda28.tourmanagingcrud.service.EventService;
import pl.sda.javagda28.tourmanagingcrud.service.VenueService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final VenueService venueService;
    private final BandService bandService;

    @GetMapping
    public String displayEvents(final ModelMap modelMap) {
        List<Event> events = eventService.getAllEvents();

        modelMap.addAttribute("events", events);

        return "event-list";
    }
    @Secured({"ROLE_ADMIN", "ROLE_ORGANISER"})
    @GetMapping("/add")
    public String viewEventForm(final ModelMap modelMap) {
        List<Event> events = eventService.getAllEvents();
        List<Venue> venues = venueService.getAllVenues();
        List<Band> bands = bandService.getAllBands();
        modelMap.addAttribute("venues", venues);
        modelMap.addAttribute("bands", bands);
        modelMap.addAttribute("event-form", new EventForm());
        modelMap.addAttribute("events", events);
        modelMap.addAttribute("method", "add");

        return "event-form";
    }
    @Secured({"ROLE_ADMIN", "ROLE_ORGANISER"})
    @PostMapping("/add")
    public String saveEvent(@Valid @ModelAttribute final EventForm eventForm, final ModelMap modelMap) {
        eventService.createEvent(eventForm);
        return displayEvents(modelMap);
    }
    @Secured({"ROLE_ADMIN", "ROLE_ORGANISER"})
    @PostMapping("/remove/{id}")
    public String removeEvent(@PathVariable("id") final Long id, final ModelMap modelMap) {
        eventService.removeEvent(id);
        return displayEvents(modelMap);
    }
    @Secured({"ROLE_ADMIN", "ROLE_ORGANISER"})
    @GetMapping("/edit/{id}")
    public String getEventEditForm(@PathVariable("id") final Long id, final ModelMap modelMap) {
        List<Venue> venues = venueService.getAllVenues();
        List<Band> bands = bandService.getAllBands();
        modelMap.addAttribute("venues", venues);
        modelMap.addAttribute("bands", bands);
        modelMap.addAttribute("event-form", eventService.createEventFormById(id));
        modelMap.addAttribute("method", "edit/" + id);

        return "event-form";
    }
    @Secured({"ROLE_ADMIN", "ROLE_ORGANISER"})
    @PostMapping("/edit/{id}")
    public String editEvent(@Valid @ModelAttribute final ModelMap modelMap, @PathVariable("id") final Long id, final EventForm eventForm) {
        eventService.updateEvent(id, eventForm);
        return displayEvents(modelMap);
    }
}
