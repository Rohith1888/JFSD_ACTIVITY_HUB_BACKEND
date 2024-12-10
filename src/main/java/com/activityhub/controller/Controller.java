package com.activityhub.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Controller {

	
	@GetMapping("/")
	public String  welcome()
	{
		return "<h1>Your Viewing the Backend of Our Project Activity Hub</h1>"+"<h3> Section 28 - Team 6</h3>"+"<h2>Team Members:</h2> <ol><li>Gopisetty Rohith : 2200032314 </li><li>Gurrala Jeevana Koushika : 2200031073 </li> <li>K Chashmitha : 2200030980 </li><ol>";
	}
}
