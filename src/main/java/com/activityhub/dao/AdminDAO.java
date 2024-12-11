package com.activityhub.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.activityhub.demo.Admin;
import com.activityhub.demo.AdminInterface;
import com.activityhub.demo.Club;
import com.activityhub.demo.ClubInterface;

@Component
public class AdminDAO {
	@Autowired
	AdminInterface adminRepo;
	
	@Autowired
	ClubInterface clubRepo;
	
	public Admin findByEmail(String email)
	{
		return adminRepo.findByEmail(email);
	}
	
	public Admin findByFullNam(String name)
	{
		return adminRepo.findByFullName(name);
	}

	 public Admin findUser(String email)
		{
			return adminRepo.findByEmail(email);
		}
	 public Club addClub( Club club)
	 {
			 return clubRepo.save(club);
				
	 }
	 
	 public List<Club> getAllClubs()
	 {
		 return clubRepo.findAll();
	 }

	public void updateClub(Club club) {
		// TODO Auto-generated method stub
		clubRepo.save(club);
		
	}

	public void deleteClub(Club club) {
		// TODO Auto-generated method stub
		clubRepo.delete(club);
		
	}
	
	 
	 
	
	 
}
