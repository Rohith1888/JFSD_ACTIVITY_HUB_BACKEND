package com.activityhub.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventInterface extends JpaRepository<Event, Integer> {
    List<Event> findByClubId(int clubId); // Find events by club ID
    List<Event> findByOrganizerEmail(String email); // Find events by organizer's email
}
