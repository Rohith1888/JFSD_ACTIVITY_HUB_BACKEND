package com.activityhub.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.activityhub.demo.Event;
import com.activityhub.demo.Student;
import com.activityhub.demo.StudentInterface;

@Component
public class StudentDAO {
	@Autowired
	StudentInterface studentRepository;
	 public Student findByEmail(String email) {
		 
	        return studentRepository.findByEmail(email);
	    }
	    
	 
	 public void updateStudent(Student student)
	 {
		 studentRepository.save(student);
	 }
	 public List<Student> getAllStudentsOfClub(Integer clubId) {
		    return studentRepository.findByClubId(clubId);
		}


	    public void saveStudent(Student student) {
	        studentRepository.save(student);
	    }
	    public Student findUser(String email)
		{
			return studentRepository.findByEmail(email);
		}
	    
	    public void UpdateStudent(Student student)
	    {
	    	studentRepository.save(student);
	    }
		 public List<Student> getAllStudents()
		 {
			 return studentRepository.findAll();
		 }

		 
		 public Student findByEmailStudent(String email) {
		        return studentRepository.findByEmail(email);
		 }
		 
		 public Student findByIdNumber(String id)
		 {
			 return studentRepository.findByIdNumber(id);
		 }
		 
		 public void deleteStudent(String email)
		 {
			 studentRepository.delete(studentRepository.findByEmail(email));
		 }
		  public List<Event> getOrganizingEvents(String studentEmail) {
		        return studentRepository.findOrganizingEventsByStudentEmail(studentEmail);
		    }
	 
	 

}
