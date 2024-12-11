package com.activityhub.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminInterface extends JpaRepository<Admin, String> {
	
	
	 Admin findByEmail(String email);
	 Admin findByFullName(String name);
	 
}
