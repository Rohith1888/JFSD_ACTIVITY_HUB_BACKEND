package com.activityhub.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.activityhub.dao.AdminDAO;
import com.activityhub.dao.StudentDAO;

@RestController
@CrossOrigin
public class LoginController {
	
	@Autowired
	StudentDAO studentDao;
	
	@Autowired
	AdminDAO adminDao;
	


}
