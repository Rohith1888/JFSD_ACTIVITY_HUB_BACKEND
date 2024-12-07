package com.activityhub.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubInterface extends JpaRepository<Club,Integer>{
	public Club findByName(String name);
	public Club findById(int id);
}
