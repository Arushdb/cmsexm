package edu.dei.examination.phd.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScholarDTO {
   
    private String appno;
    private String firstName;
    private String category;
    
    private String email;
    private String phone;
       
    private int gender_id ;
    private LocalDate dob;
    private int programid;
    private String programname;
    
    
  
    
    private LocalDate admissionDate;
    
    
    

    

    public ScholarDTO(String appno, String firstName, String category, String email, String phone, int gender_id,
			LocalDate dob, int programid, LocalDate admissionDate) {
		
		this.appno = appno;
		this.firstName = firstName;
		this.category = category;
		this.email = email;
		this.phone = phone;
		this.gender_id = gender_id;
		this.dob = dob;
		this.programid = programid;
		this.admissionDate = admissionDate;
	}

	public ScholarDTO() {}

    // getters & setters
 

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

  
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

	public String getAppno() { return appno; }

	public void setAppno(String appno) { this.appno = appno; }

	public String getCategory() { return category; }

	public void setCategory(String category) { this.category = category; }

	public int getGender_id() { return gender_id; }

	public void setGender_id(int gender_id) { this.gender_id = gender_id; }

	
	public LocalDate getAdmissionDate() { return admissionDate; }

	public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

	public LocalDate getDob() { return dob; }

	public void setDob(LocalDate dob) { this.dob = dob; }

	public int getProgramid() { return programid; }

	public void setProgramid(int programid) { this.programid = programid; }

	public String getProgramname() { return programname; }

	public void setProgramname(String programname) { this.programname = programname; }
	
	
    

  
}
