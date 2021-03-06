package pl.sda.javagda28.tourmanagingcrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.javagda28.tourmanagingcrud.entity.Band;
import pl.sda.javagda28.tourmanagingcrud.entity.Event;
import pl.sda.javagda28.tourmanagingcrud.entity.Venue;
import pl.sda.javagda28.tourmanagingcrud.model.EventForm;
import pl.sda.javagda28.tourmanagingcrud.repository.BandRepository;
import pl.sda.javagda28.tourmanagingcrud.repository.EventRepository;
import pl.sda.javagda28.tourmanagingcrud.repository.VenueRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final BandRepository bandRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event createEvent(final EventForm eventForm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate eventDate = LocalDate.parse(eventForm.getDate(), formatter);

        Optional<Venue> venue = venueRepository.findById(eventForm.getVenueId());
        List<Band> bandsByIds = bandRepository.findByIdIn(eventForm.getBandIds());


        Event event = new Event(null, eventForm.getName(), LocalDateTime.of(eventDate, LocalTime.of(20, 0)), bandsByIds, venue.get());
        return eventRepository.saveAndFlush(event);
    }

    public void removeEvent(final Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public void updateEvent(final Long id, final EventForm eventForm) {
        Optional<Event> event = eventRepository.findById(id);
        Event e = event.get();
        e.setName(eventForm.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate eventDate = LocalDate.parse(eventForm.getDate(), formatter);
        LocalTime localTime = LocalTime.of(20, 0);
        LocalDateTime localDateTime = LocalDateTime.of(eventDate, localTime);
        e.setDate(localDateTime);
        e.setBands(bandRepository.findByIdIn(eventForm.getBandIds()));
        e.setVenue(venueRepository.findById(eventForm.getVenueId()).get());
        eventRepository.save(e);
    }

    public EventForm createEventFormById (final Long id){
        Event event = eventRepository.findById(id).get();

        EventForm eventForm = new EventForm();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Long> bandIds = event.getBands().stream()
                .map(band -> band.getId())
                .collect(Collectors.toList());

        return eventForm.builder().name(event.getName()).date(event.getDate().format(formatter))
                .venueId(event.getVenue().getId()).bandIds(bandIds).build();
    }
}
