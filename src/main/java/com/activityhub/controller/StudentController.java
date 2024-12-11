package com.activityhub.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.activityhub.dao.EventDAO;
import com.activityhub.dao.StudentDAO;
import com.activityhub.demo.AttendanceRequest;
import com.activityhub.demo.Club;
import com.activityhub.demo.ClubInterface;
import com.activityhub.demo.Event;
import com.activityhub.demo.Student;

import jakarta.activation.FileDataSource;
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
	
	@Autowired
	EventDAO eventRepository;
	
	@PostMapping("/signup")
	public String signUp(@RequestBody Student student) {
	    if (studentDao.findByEmail(student.getEmail()) != null) {
	        return "Email already exists";
	    }
	    // Set default profile image (Base64 encoded)
	    String defaultImageBase64 = getDefaultProfileImageBase64();
	    student.setProfileImage(defaultImageBase64);
	   
		String toemail=student.getEmail();
		String subject = "Welcome to Activity Hub 🎉";

		String text = "<html><body>"
		             + "<h2>Welcome to Activity Hub!</h2>"
		             + "<p>Congratulations! You have successfully registered your account in Activity Hub.</p>"
		             + "<p>We're excited to have you as part of our community. Get ready to explore events, join clubs, and engage in activities that interest you.</p>"
		             + "<p>To get started, please log in to your account using the link below:</p>"
		             + "<p><a href='https://activityhubklu.netlify.app/signin'>Login to Activity Hub</a></p>"
		             + "<p>If you have any questions, feel free to reach out to our support team.</p>"
		             + "<br><br>"
		             + "<img src='cid:logo' alt='Activity Hub Logo' width='250' height='50'/>"
		             + "<br><br>"
		             + "<p>Thank you for joining us,<br>The Activity Hub Team</p>"
		             + "</body></html>";

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
        String fromEmail = "activityhubjfsdklu@gmail.com";
        
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text, true);
        FileDataSource fds = new FileDataSource("./src/main/resources/static/logo_header.png");  // Image path
        helper.addInline("logo", fds);
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
    	 System.out.println("OTP Map: " + otpMap);

         // Send email with OTP
         String toEmail = email;
         String subject = "Your OTP for registration to Activity Hub 😊";
         String text = "<html><body>"
                     + "<h2>Your OTP from Activity Hub</h2>"
                     + "<p>Your OTP: <strong>" + otp + "</strong></p>"
                     + "<p>This OTP is provided by Activity Hub to authenticate your access or verify your account. " +
                       "Please use this One-Time Password within the time provided on the website to complete your action. " +
                       "Do not share this OTP with anyone for security reasons.</p>"
                     + "<p>If you did not request this OTP, please ignore this email or contact our support team immediately.</p>"
                     + "<br><br>"
                     + "<img src='cid:logo' alt='Activity Hub Logo' width='250' height='50'/>"
                     + "<br><br>"
                     + "<p>Thank you,<br>The Activity Hub Team</p>"
                     + "</body></html>";

         try {
             sendEmail(toEmail, subject, text);
             return "OTP Sent successfully";
         } catch (MessagingException e) {
             return "Error while sending mail: " + e.getMessage();
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

    	String text = "<html><body>"
    	             + "<h2>Password Reset OTP from Activity Hub</h2>"
    	             + "<p>You requested a password reset for your Activity Hub account. Please use the OTP below to proceed with resetting your password:</p>"
    	             + "<p>Your OTP: <strong>" + otp + "</strong></p>"
    	             + "<p>This OTP is valid for a limited time. Please use it as soon as possible to reset your password.</p>"
    	             + "<p>If you did not request a password reset, please ignore this email or contact our support team immediately.</p>"
    	             + "<br><br>"
    	             + "<img src='cid:logo' alt='Activity Hub Logo' width='250' height='50'/>"  // Adjust the image dimensions as needed
    	             + "<br><br>"
    	             + "<p>Thank you,<br>The Activity Hub Team</p>"
    	             + "</body></html>";

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

		    
		    @GetMapping("/{email}")
		    public Student findStudentByEmail(@PathVariable String email)
		    {
		    	return studentDao.findByEmailStudent(email);
		    }
		    
		    @PostMapping("/{eventId}/markAttendance")
		    public String markAttendance(@PathVariable int eventId, @RequestBody AttendanceRequest attendanceRequest) {
		        // Fetch event by eventId
		        Event event = eventRepository.getEventById(eventId);
		        if (event == null) {
		            return "Event not found";
		        }

		        // Get the list of students who attended and who didn't attend
		        List<String> attendedEmails = attendanceRequest.getAttendedEmails();
		        List<String> nonAttendedEmails = attendanceRequest.getNotAttendedEmails();

		        if (attendedEmails == null) attendedEmails = new ArrayList<>();
		        if (nonAttendedEmails == null) nonAttendedEmails = new ArrayList<>();

		        // Debugging logs
		        System.out.println("Attended Emails: " + attendedEmails);
		        System.out.println("Non-attended Emails: " + nonAttendedEmails);

		        // Fetch all students or filter based on event registration (if necessary)
		        List<Student> students = studentDao.getAllStudents();
		        for (Student student : students) {
		            String studentEmail = student.getEmail().trim(); // Ensure no extra spaces

		            // Add points for attended students
		            if (attendedEmails.stream().anyMatch(email -> email.trim().equalsIgnoreCase(studentEmail))) {
		                student.setPoints(student.getPoints() + event.getPoints());
		                System.out.println("Points added to: " + student.getFullName() + ", Points: " + student.getPoints());
		            } 
		            // Deduct penalty for non-attended students
		            else if (nonAttendedEmails.stream().anyMatch(email -> email.trim().equalsIgnoreCase(studentEmail))) {
		                student.setPoints(student.getPoints() - event.getPenalty());
		                System.out.println("Penalty deducted from: " + student.getFullName() + ", Points: " + student.getPoints());
		            }
		            studentDao.updateStudent(student); // Save updated student points
		        }

		        return "Attendance marked successfully!";
		    }
		    
		    @GetMapping("/getPoints/{email}")
		    public int getPoints(@PathVariable String email)
		    {
		    	int points = studentDao.findByEmailStudent(email).getPoints();
		    	return points;
		    }

		    
		    @PostMapping("/sendContactMessage")
		    public String sendContactMessage(@RequestBody Map<String, String> contactDetails)
		    {
		        String name = contactDetails.get("name");
		        String email = contactDetails.get("email");
		        String message = contactDetails.get("message");

		        // Check if email is valid or exists
		        if (email == null || email.isEmpty()) {
		            return "Invalid email address.";
		        }

		        // Here you can also check if the message is too short or if other fields are valid

		        String subject = "Thank you for contacting Activity Hub!";
		        String text = "<html><body>"
		                     + "<h2>Contact Message Acknowledgment from Activity Hub</h2>"
		                     + "<p>Dear " + name + ",</p>"
		                     + "<p>Thank you for reaching out to Activity Hub. We have received your message:</p>"
		                     + "<p><strong>Message:</strong><br>" + message + "</p>"
		                     + "<p>Our team will review your inquiry and get back to you as soon as possible.</p>"
		                     + "<p>If you have any urgent concerns, feel free to contact us directly at our support email.</p>"
		                     + "<br><br>"
		                     +"<img src='cid:logo' alt='Activity Hub Logo' width='250' height='50'/>\""
		                     + "<p>Thank you for contacting us,<br>The Activity Hub Team</p>"
		                     + "</body></html>";

		        try {
		            sendEmail(email, subject, text);
		            return "Your message has been sent successfully.";
		        } catch (MessagingException e) {
		            // Handle exception
		            return "Error while sending confirmation email: " + e.getMessage();
		        }
		    }

		    
	 

}
