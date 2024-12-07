package com.activityhub.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

	private int numberOfStudents;
    private String category;
    @Lob // Indicates a large object stored in the database
    private byte[] clubImage;
    
    String venue;
    String clubMail;
    

    public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getClubMail() {
		return clubMail;
	}

	public void setClubMail(String clubMail) {
		this.clubMail = clubMail;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
    
    public String getClubImage() {
        // Convert byte[] profileImage to Base64 string for frontend display
        if (clubImage != null) {
            return Base64.getEncoder().encodeToString(clubImage);
        }
        return null;
    }

    public void setClubImage(String clubImageBase64) {
        // Decode the Base64 string to byte[] and store it
        if (clubImageBase64 != null && !clubImageBase64.isEmpty()) {
            this.clubImage = Base64.getDecoder().decode(clubImageBase64);
        } else {
            this.clubImage = null;
        }
    }
}
