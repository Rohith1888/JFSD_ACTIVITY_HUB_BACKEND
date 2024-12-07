package com.activityhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.activityhub.dao.StudentDAO;
import com.activityhub.demo.Event;
import com.activityhub.demo.EventInterface;
import com.activityhub.demo.Registration;
import com.activityhub.demo.RegistrationRepository;
import com.activityhub.demo.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/registrations")
public class RegistrationController {

	@Autowired
	private EventInterface eventRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentDAO studentRepository;

    // Endpoint to register for an event
    @PostMapping("/{studentEmail}/register/{eventId}")
    public String registerForEvent(@PathVariable String studentEmail, @PathVariable Integer eventId) {
        // Fetch the student
        Student student = studentRepository.findByEmail(studentEmail);
        if (student == null) {
            return "Student not found.";
        }

        // Check if already registered for the event
        List<Registration> existingRegistrations = registrationRepository.findByStudentEmailAndEventId(studentEmail, eventId);
        if (!existingRegistrations.isEmpty()) {
            return "Student is already registered for this event.";
        }

        // Create new registration
        Registration registration = new Registration();
        registration.setStudentEmail(studentEmail);
        registration.setFullName(student.getFullName());
        registration.setEventId(eventId);
        registration.setRegistrationTime(LocalDateTime.now());

        registrationRepository.save(registration);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return "Event not found.";
        }

        // Add student email to the registeredStudentEmails list
        List<String> registeredEmails = event.getRegisteredStudentEmails();
        if (registeredEmails == null) {
            registeredEmails = new ArrayList<>();
        }
        registeredEmails.add(studentEmail);
        event.setRegisteredStudentEmails(registeredEmails);

        // Save the updated event
        eventRepository.save(event);
        return "Successfully registered for the event.";
    }

    @PostMapping("/{studentEmail}/cancel/{eventId}")
    public String cancelRegistration(@PathVariable String studentEmail, @PathVariable Integer eventId) {
        // Fetch the registration
        List<Registration> registrations = registrationRepository.findByStudentEmailAndEventId(studentEmail, eventId);
        if (registrations.isEmpty()) {
            return "No registration found for this event.";
        }

        // Delete the registration
        registrationRepository.deleteAll(registrations);

        // Fetch the event
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return "Event not found.";
        }

        // Remove the student's email from the registeredStudentEmails list
        List<String> registeredEmails = event.getRegisteredStudentEmails();
        if (registeredEmails != null && registeredEmails.contains(studentEmail)) {
            registeredEmails.remove(studentEmail);
            event.setRegisteredStudentEmails(registeredEmails);
            // Save the updated event
            eventRepository.save(event);
        }

        return "Successfully canceled the registration.";
    }

    // Endpoint to get all registered events for a student
    @GetMapping("/{studentEmail}")
    public List<Registration> getRegisteredEvents(@PathVariable String studentEmail) {
        return registrationRepository.findByStudentEmail(studentEmail);
    }
}
