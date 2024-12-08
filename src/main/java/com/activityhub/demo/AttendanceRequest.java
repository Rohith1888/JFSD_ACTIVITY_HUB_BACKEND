package com.activityhub.demo;

import java.util.List;


public class AttendanceRequest {
    private int eventId;
    private List<String> attendedEmails;
    private List<String> notAttendedEmails;
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public List<String> getAttendedEmails() {
		return attendedEmails;
	}
	public void setAttendedEmails(List<String> attendedEmails) {
		this.attendedEmails = attendedEmails;
	}
	public List<String> getNotAttendedEmails() {
		return notAttendedEmails;
	}
	public void setNotAttendedEmails(List<String> notAttendedEmails) {
		this.notAttendedEmails = notAttendedEmails;
	}

    // Getters and Setters
}
