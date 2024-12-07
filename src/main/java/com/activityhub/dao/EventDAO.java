package com.activityhub.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.activityhub.demo.Event;
import com.activityhub.demo.EventInterface;

import java.util.List;

@Repository
public class EventDAO {

    @Autowired
    private EventInterface eventRepository;

    // Add a new event
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    // Get an event by ID
    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get events by Club ID
    public List<Event> getEventsByClubId(int clubId) {
        return eventRepository.findByClubId(clubId);
    }

    // Get events by Organizer's email
    public List<Event> getEventsByOrganizerEmail(String email) {
        return eventRepository.findByOrganizerEmail(email);
    }

    // Update an existing event
    public Event updateEvent(Event event) {
        if (eventRepository.existsById(event.getId())) {
            return eventRepository.save(event);
        }
        return null; // Event not found
    }

    // Delete an event by ID
    public boolean deleteEvent(int id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false; // Event not found
    }
}
