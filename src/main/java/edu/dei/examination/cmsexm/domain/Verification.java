package edu.dei.examination.cmsexm.domain;

import java.math.BigDecimal;
import java.util.Date;


public class Verification {

	
	private String enrollment_number;
	private String roll_number;
	

	private java.math.BigDecimal cgpa;


	private Date  passed_from_session;


	private String student_first_name;
	

	private String component_description;

	
	private String program_name ;

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

		
	public java.math.BigDecimal getCgpa() {
		return cgpa;
	}

	public void setCgpa(java.math.BigDecimal cgpa) {
		this.cgpa = cgpa;
	}
	
	public String getStudent_first_name() {
		return student_first_name;
	}

	public void setStudent_first_name(String student_first_name) {
		this.student_first_name = student_first_name;
	}


	public String getComponent_description() {
		return component_description;
	}

	public void setComponent_description(String component_description) {
		this.component_description = component_description;
	}

	

	public String getProgram_name() {
		return program_name;
	}

	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}

	
	public Date getPassed_from_session() {
		return passed_from_session;
	}

	public void setPassed_from_session(Date passed_from_session) {
		this.passed_from_session = passed_from_session;
	}
	public Verification() {
		
	}

	public Verification(String enrollment_number, String roll_number, BigDecimal cgpa, Date passed_from_session,
			String student_first_name, String component_description, String program_name) {
		
		this.enrollment_number = enrollment_number;
		this.roll_number = roll_number;
		this.cgpa = cgpa;
		this.passed_from_session = passed_from_session;
		this.student_first_name = student_first_name;
		this.component_description = component_description;
		this.program_name = program_name;
	}

	

	
	


	

	
	

}
