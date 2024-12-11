package com.activityhub.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.activityhub.dao.AdminDAO;
import com.activityhub.dao.EventDAO;
import com.activityhub.dao.StudentDAO;
import com.activityhub.demo.Admin;
import com.activityhub.demo.Club;
import com.activityhub.demo.ClubInterface;
import com.activityhub.demo.Event;
import com.activityhub.demo.Student;
@RestController
@CrossOrigin
@RequestMapping("admin/")
public class AdminController {
	
	@Autowired
	AdminDAO adminDao;
	
	@Autowired
	StudentDAO studentDao;
	
	@Autowired
	ClubInterface clubRepository;
	
	@Autowired
	EventDAO eventDao;
	
	 @PostMapping("/addClub")
	 public String addClub(@RequestBody Club club)
	 {
		
		 if(clubRepository.findByName(club.getName())==null)
		 {
			 adminDao.addClub(club);
			 return "Club added Successfully";
		 }
		 else return "Club already exits";
	 }
	 
	 @GetMapping("/getAllClubs")
	 public List<Club> getAllClubs()
	 {
		 return adminDao.getAllClubs();
	 }
	 @PutMapping("/updateClub")
	    public String updateClub(@RequestBody Club club) {
		
	        if (clubRepository.findById(club.getId()) == null) {
	            return "Club not found";
	        } else {
	            adminDao.updateClub(club);
	            return "Club updated successfully";
	        }
	    }
	 @DeleteMapping("/deleteClub")
	 public String deleteClub(@RequestBody Club club)
	 {
		 if (clubRepository.findById(club.getId()) == null) {
	            return "Club not found";
	        } else {
	            adminDao.deleteClub(club);
	            return "Student updated successfully";
	        }
	 }
	 
	 @PostMapping("/login")
	 public Object login(@RequestBody Map<String, String> credentials) {
	     String identifier = credentials.get("email"); // Can be email or ID number
	     String password = credentials.get("password");

	     // Determine if the identifier is an email (contains '@') or an ID number
	     Student st = null;
	     Admin at = null;

	     if (identifier.contains("@")) {
	         // Assume it's an email
	         st = studentDao.findByEmail(identifier);
	         at = adminDao.findByEmail(identifier);
	         
	     } else {
	         // Assume it's an ID number
	    	 at = adminDao.findByFullNam(identifier);
	         st = studentDao.findByIdNumber(identifier);
	     }

	     // Check credentials
	     if (st != null && st.getPassword().equals(password)) {
	         System.out.println("Student logged in");
	         return st;
	     } else if (at != null && at.getPassword().equals(password)) {
	         System.out.println("Admin logged in");
	         return at;
	     } else {
	         System.out.println("Invalid credentials");
	         return null; // Or you could return an appropriate HTTP response
	     }
	 }

		

		    // Endpoint to fetch details of a specific club
		    @GetMapping("/{clubId}")
		    public Club getClubById(@PathVariable int clubId) {
		        return clubRepository.findById(clubId);
		    }

		    // Endpoint to fetch all students in a specific club
		    @GetMapping("/{clubId}/students")
		    public List<Student> getStudentsInClub(@PathVariable int clubId) {
		        List<Student> students = studentDao.getAllStudentsOfClub(clubId);
		        if (students.isEmpty()) {
		            return null;
		        }
		        return students;
		    }
		    
		    
		    @PostMapping("/addEvent")
		    public String addEvent(@RequestBody Event event) {
		        if (clubRepository.getById(event.getClubId()) == null) {
		            return "Club not found. Cannot add event.";
		        }
		        eventDao.addEvent(event);
		        return "Event added successfully.";
		    }

		    // Get All Events
		    @GetMapping("/getAllEvents")
		    public List<Event> getAllEvents() {
		        return eventDao.getAllEvents();
		    }

		    // Get Event by ID
		    @GetMapping("/getEvent/{eventId}")
		    public Event getEventById(@PathVariable int eventId) {
		        Event event = eventDao.getEventById(eventId);
		        if (event == null) {
		            throw new RuntimeException("Event not found with ID: " + eventId);
		        }
		        return event;
		    }

		    // Update Event
		    @PutMapping("/updateEvent")
		    public String updateEvent(@RequestBody Event event) {
		        if (eventDao.getEventById(event.getId()) == null) {
		            return "Event not found.";
		        }
		        eventDao.updateEvent(event);
		        return "Event updated successfully.";
		    }

		    // Delete Event
		    @DeleteMapping("/deleteEvent/{id}")
		    public String deleteEvent(@PathVariable int id) {
		        if (!eventDao.deleteEvent(id)) {
		            return "Event not found or could not be deleted.";
		        }
		        return "Event deleted successfully.";
		    }

		    // Get Events by Club
		    @GetMapping("/getEventsByClub/{clubId}")
		    public List<Event> getEventsByClub(@PathVariable int clubId) {
		        return eventDao.getEventsByClubId(clubId);
		    }

		    // Get Events by Organizer
		    @GetMapping("/getEventsByOrganizer/{organizerEmail}")
		    public List<Event> getEventsByOrganizer(@PathVariable String organizerEmail) {
		        return eventDao.getEventsByOrganizerEmail(organizerEmail);
		    }

		    
		    

	 
}
