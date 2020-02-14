package pl.sda.javagda28.tourmanagingcrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.javagda28.tourmanagingcrud.dto.VenueForm;
import pl.sda.javagda28.tourmanagingcrud.entity.Event;
import pl.sda.javagda28.tourmanagingcrud.entity.Venue;
import pl.sda.javagda28.tourmanagingcrud.repository.EventRepository;
import pl.sda.javagda28.tourmanagingcrud.repository.VenueRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public List<Venue> getAllVenues(){
        return venueRepository.findAll();
    }

    public Venue createVenue(final VenueForm venueForm){

        List<Event> byIdIn = eventRepository.findByIdIn(venueForm.getEventIds());

        Venue venue = new Venue(null, venueForm.getName(), venueForm.getAddress(), byIdIn);

        return venueRepository.save(venue);
    }


}
