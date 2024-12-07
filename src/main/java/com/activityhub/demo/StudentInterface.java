package com.activityhub.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentInterface extends JpaRepository<Student, String> {
    // Find a student by email
    Student findByEmail(String email);

    // Find all students by clubId
    @Query("SELECT s FROM Student s WHERE s.club_id = :clubId")
    List<Student> findByClubId(@Param("clubId") Integer clubId);

    // Find all events organized by a student
    @Query("SELECT e FROM Event e WHERE e.organizerEmail = :email")
    List<Event> findOrganizingEventsByStudentEmail(@Param("email") String email);
}
