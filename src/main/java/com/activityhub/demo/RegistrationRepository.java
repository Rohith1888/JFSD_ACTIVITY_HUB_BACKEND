package com.activityhub.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByStudentEmail(String studentEmail);
    List<Registration> findByStudentEmailAndEventId(String studentEmail, Integer eventId);
    List<Registration> findByEventId(Integer eventId);
}
