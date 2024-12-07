package com.activityhub.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.activityhub.dao.StudentDAO;
import com.activityhub.demo.Club;
import com.activityhub.demo.ClubInterface;
import com.activityhub.demo.Event;
import com.activityhub.demo.Student;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@CrossOrigin()
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	StudentDAO studentDao;
	
	@Autowired
	ClubInterface clubRepository;
	
	@Autowired
	JavaMailSender jm;
	
	@PostMapping("/signup")
	public String signUp(@RequestBody Student student) {
	    if (studentDao.findByEmail(student.getEmail()) != null) {
	        return "Email already exists";
	    }
	    // Set default profile image (Base64 encoded)
	    String defaultImageBase64 = getDefaultProfileImageBase64();
	    student.setProfileImage(defaultImageBase64);
	   
		String toemail=student.getEmail();
		String subject = "Welcome to Activity Hub";
	    String text = "You have succssfull registered your account in Activity Hub";
	    try {
            sendEmail(student.getEmail(), subject, text);
        } catch (MessagingException e) {
            return "Error sending  email: " + e.getMessage();
        }

	    studentDao.saveStudent(student);
	    return "Student registered successfully";
	}

	private String getDefaultProfileImageBase64() {
	    try {
	        // Read the default image file and encode it as Base64
	        byte[] imageBytes = Files.readAllBytes(Paths.get("./src/main/resources/static/default.jpg"));
	        return Base64.getEncoder().encodeToString(imageBytes);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	
	@PutMapping("/updateProfile")
	public String updateProfileImage(@RequestBody Student student)
	{
		Student newStudent = studentDao.findByEmail(student.getEmail());
		newStudent.setProfileImage(student.getProfileImage());
		newStudent.setFullName(student.getFullName());
		newStudent.setIdNumber(student.getIdNumber());
		newStudent.setBatch(student.getBatch());
		newStudent.setDegree(student.getDegree());
		newStudent.setPhoneNumber(student.getPhoneNumber());
		studentDao.saveStudent(newStudent)	;
		return "Student Image Updated";
	}
	
    private void sendEmail(String toEmail, String subject, String text) throws MessagingException {
        MimeMessage message = jm.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        // Sender's email address
        String fromEmail = "acivityhubmailservice@gmail.com";
        
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text);
        
        jm.send(message);
    }
    private String generateOTP() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    HashMap<String, String> otpMap = new HashMap<String,String>();
    @PostMapping("/sendOtp")
    public String sendOtp(@RequestBody String email)
    {
    	if(studentDao.findByEmail(email)!=null)
    	{
    		return "Email already exists";
    	}
    	String otp = generateOTP();
    	otpMap.put(email, otp);
    	System.out.println(otpMap);
    	String toEmail = email;
    	String subject = "Your OTP for registration";
    	String text = "Your OTP : "+otp;	
    	try {
			sendEmail(toEmail, subject, text);
			return "OTP Sent successfully";
		} catch (MessagingException e) {
			// TODO: handle exception
			return "Error while sending mail"+e.getMessage();
		}
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestBody OTPHandler otpHandler) {
        // Check if the email exists in the map
    	System.out.println(otpHandler.getEmail() + " "+ otpHandler.getOtp());
    	System.out.println(otpMap);
        if (otpMap.containsKey(otpHandler.getEmail())) {
            // Verify the OTP
            if (otpMap.get(otpHandler.getEmail()).equals(otpHandler.getOtp())) {
                // OTP is correct; remove the entry and return success
                otpMap.remove(otpHandler.getEmail());
                return "OTP verified successfully!";
            } else {
                // OTP is incorrect
                return "Invalid OTP!";
            }
        } else {
            // Email not found
            return "No OTP found for the provided email!";
        }
    }

    
    @PostMapping("/sendOtpforgot")
    public String sendOtpForgot(@RequestBody String email)
    {
    	if(studentDao.findByEmail(email)==null)
    	{
    		return "Email does not exists";
    	}
    	String otp = generateOTP();
    	otpMap.put(email, otp);
    	System.out.println(otpMap);
    	String toEmail = email;
    	String subject = "Your OTP for password reset";
    	String text = "Your OTP : "+otp;	
    	try {
			sendEmail(toEmail, subject, text);
			return "OTP Sent successfully";
		} catch (MessagingException e) {
			// TODO: handle exception
			return "Error while sending mail"+e.getMessage();
		}
    }
	
    @PostMapping("/change")
    public String resetPassword(@RequestBody OTPHandler data)
    {
    	Student st = studentDao.findByEmail(data.getEmail());
    	st.setPassword(data.getPassword());
    	studentDao.saveStudent(st);
    	return "Password Changed";
    }
    
    @PostMapping("/addStudent")
	public String addStudent(@RequestBody Student student)
	{
		if(studentDao.findByEmail(student.getEmail())!= null)
		{
			return "Email already exits";
		}
		else
		{
			String defaultImageBase64 = getDefaultProfileImageBase64();
		    student.setProfileImage(defaultImageBase64);
			studentDao.saveStudent(student);
			return "Student added Successfully";
		}
	}
    
	@GetMapping("/all")
	public List<Student> getAllStudents()
	{
		return studentDao.getAllStudents();
	}
	
	 @PutMapping("/updateStudent")
	    public String updateStudent(@RequestBody Student student) {
	        if (studentDao.findByEmailStudent(student.getEmail()) == null) {
	            return "Student not found";
	        } else {
	            studentDao.updateStudent(student);
	            return "Student updated successfully";
	        }
	    }
	 
	 
	 @DeleteMapping("/deleteStudent")
	 public String deleteStudent(@RequestBody String email)
	 {
		 if(studentDao.findByEmailStudent(email)!=null)
		 {
			 studentDao.deleteStudent(email);
			 return "Student Deleted Successfully";
		 }
		 else return "Student not found";
	 }
	 
	 
	 @PostMapping("/{studentEmail}/join/{clubId}")
	 public String joinClub(@PathVariable String studentEmail, @PathVariable int clubId) {
	     // Fetch the student
	     Student student = studentDao.findByEmail(studentEmail);
	     if (student == null) {
	         return "Student not found";
	     }

	     // Fetch the club
	     Club club = clubRepository.findById(clubId);
	     if (club == null) {
	         return "Club not found";
	     }

	     // Check if the student is already in a club
	     if (student.getClub_id() != null) {
	         return "Leave your current club before joining another one.";
	     }

	     // Update student and club
	     student.setClub_id(club.getId());
	     studentDao.updateStudent(student);

	     // Increment the number of students in the club
	     club.setNumberOfStudents(club.getNumberOfStudents() + 1);
	     
	     // Save the updated club
	     clubRepository.save(club);

	     // Debugging output
	     System.out.println("Updated number of students: " + club.getNumberOfStudents());
	     return "Student successfully joined the club.";
	 }

	
		    // Endpoint for a student to leave a club
		    @PostMapping("/{studentEmail}/leave")
		    public String leaveClub(@PathVariable String studentEmail) {
		        // Fetch the student
		        Student student = studentDao.findByEmail(studentEmail);
		        if (student == null) {
		            return "Student does not exist";
		        }
	
		        // Check if the student is part of a club
		        Integer clubId = student.getClub_id();
		        if (clubId == null) {
		            return "Student is not part of any club.";
		        }
	
		        // Fetch the club
		        Club club = clubRepository.findById(clubId).orElse(null);
		        if (club == null) {
		            return "Club not found.";
		        }
	
		        // Remove student from the club
		        student.setClub_id(null);
		        studentDao.updateStudent(student);
	
		        club.setNumberOfStudents(club.getNumberOfStudents() - 1);
		        clubRepository.save(club);
	
		        return "Student successfully left the club.";
		    }
		    
		    @GetMapping("/getClubId/{email}")
		    public Integer getClubId(@PathVariable String email)
		    {
		    	Student st = studentDao.findByEmail(email);
		    	if(st==null)
		    	{
		    		return null;
		    	}
		    	else if(st.getClub_id()==null)
		    	{
		    		return 0;
		    	}
		    	return st.getClub_id();
		    }
		    
		    

		    @GetMapping("/{email}/organizingEvents")
		    public List<Event> getOrganizingEvents(@PathVariable String email) {
		        return studentDao.getOrganizingEvents(email);
		    }

	 

}
