package com.activityhub.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.util.Base64;

@Entity
public class Student {
    @Id
    private String email;
    private String fullName;
    private String idNumber;
    private String password;
    private String role = "student";
    private Integer club_id;

    String batch;
    String degree;
    String phoneNumber;
    private int points = 0; // Default points

    @Lob // Indicates a large object stored in the database
    private byte[] profileImage; // Store the image as a byte array

    public Integer getClub_id() {
        return club_id;
    }

    public void setClub_id(Integer club_id) {
        this.club_id = club_id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getProfileImage() {
        // Convert byte[] profileImage to Base64 string for frontend display
        if (profileImage != null) {
            return Base64.getEncoder().encodeToString(profileImage);
        }
        return null;
    }

    public void setProfileImage(String profileImageBase64) {
        // Decode the Base64 string to byte[] and store it
        if (profileImageBase64 != null && !profileImageBase64.isEmpty()) {
            this.profileImage = Base64.getDecoder().decode(profileImageBase64);
        } else {
            this.profileImage = null;
        }
    }
}
