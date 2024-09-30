package edu.dei.examination.cmsexm.model;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Transcript {
	
	@Id
	private String roll_number;
	 
	private String student_first_name;
	    private String program_name;
	    private String enrollment_number;
	    private String duration;
	    private String medium;
	    private String date_of_birth;
	    private String cgpa;
	    private String FromDate;
	    private String ToDate;
	    

	    // Getters and Setters
	    
	   	     
	     public String getEnrollment_number() {
			return enrollment_number;
		}
		public void setEnrollment_number(String enrollment_number) {
			this.enrollment_number = enrollment_number;
		}
		public String getRoll_number() {
			return roll_number;
		}
		public void setRoll_number(String roll_number) {
			this.roll_number = roll_number;
		}
		public String getStudent_first_name() {
			return student_first_name;
		}
		public void setStudent_first_name(String student_first_name) {
			this.student_first_name = student_first_name;
		}
		public String getProgram_name() {
			return program_name;
		}
		public void setProgram_name(String program_name) {
			this.program_name = program_name;
		}
		
		
		public String getDuration() { return duration; }
	    public void setDuration(String duration) { this.duration = duration; }

	    public String getMedium() { return medium; }
	    public void setMedium(String medium) { this.medium = medium; }
		
		public String getDate_of_birth() {
			return date_of_birth;
		}
		public void setDate_of_birth(String date_of_birth) {
			this.date_of_birth = date_of_birth;
		}
		public String getCgpa() {
			return cgpa;
		}
		public void setCgpa(String cgpa) {
			this.cgpa = cgpa;
		}
		public String getFromDate() {
			return FromDate;
		}
		public void setFromDate(String fromDate) {
			FromDate = fromDate;
		}
		public String getToDate() {
			return ToDate;
		}
		public void setToDate(String toDate) {
			ToDate = toDate;
		}
		
		
		
		

}
