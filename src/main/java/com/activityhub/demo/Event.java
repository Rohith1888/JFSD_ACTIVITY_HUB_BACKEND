package com.activityhub.demo;

import java.util.Base64;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String eventName;
    private String eventDescription;
    private String eventDate; // Format: YYYY-MM-DD
    private String eventTime; // Format: HH:mm
    private String eventVenue;
    private int points;
    private int penalty;
    public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	private Integer clubId; // Store club ID as Integer
    private String organizerEmail; // Store student email as String
    
    @Lob
    private byte[] eventImage; // Optional event image storage

    // List to store registered students' emails
    private List<String> registeredStudentEmails; // List of student emails

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int eventId) {
        this.id = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public List<String> getRegisteredStudentEmails() {
        return registeredStudentEmails;
    }

    public void setRegisteredStudentEmails(List<String> registeredStudentEmails) {
        this.registeredStudentEmails = registeredStudentEmails;
    }

    public String getEventImage() {
        if (eventImage != null) {
            return Base64.getEncoder().encodeToString(eventImage);
        }
        return null;
    }

    public void setEventImage(String eventImageBase64) {
        if (eventImageBase64 != null && !eventImageBase64.isEmpty()) {
            this.eventImage = Base64.getDecoder().decode(eventImageBase64);
        } else {
            this.eventImage = null;
        }
    }
}
