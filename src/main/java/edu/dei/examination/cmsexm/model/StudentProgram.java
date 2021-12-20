package edu.dei.examination.cmsexm.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

@Entity
@Table(name = "student_program",schema ="cms_live" ,catalog ="cms_live" )

@IdClass(StudentProgramPK.class)
public class StudentProgram {
	@Id
	private String enrollment_number;
	@Id
	private String roll_number;
	@Id
	private String program_status;
	@Id
	private String entity_id;
	@Id
	private String program_id;
	@Id
	private String branch_id;
	@Id
	private String specialization_id;
	
	
	
	private String current_semester;
	@Basic
	private java.math.BigDecimal cgpa;
	private String division;
	
	@Basic
    private java.sql.Date passed_from_session;
	
	


	public StudentProgram() {
		
	}
	
	



	public StudentProgram(String enrollment_number, String roll_number, String program_status, String entity_id,
			String program_id, String branch_id, String specialization_id, String current_semester, BigDecimal cgpa,
			String division, Date passed_from_session) {
		
		this.enrollment_number = enrollment_number;
		this.roll_number = roll_number;
		this.program_status = program_status;
		this.entity_id = entity_id;
		this.program_id = program_id;
		this.branch_id = branch_id;
		this.specialization_id = specialization_id;
		this.current_semester = current_semester;
		this.cgpa = cgpa;
		this.division = division;
		this.passed_from_session = passed_from_session;
	}





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



	public String getProgram_status() {
		return program_status;
	}



	public void setProgram_status(String program_status) {
		this.program_status = program_status;
	}



	public String getEntity_id() {
		return entity_id;
	}



	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}



	public String getProgram_id() {
		return program_id;
	}



	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}



	public String getBranch_id() {
		return branch_id;
	}



	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}



	public String getSpecialization_id() {
		return specialization_id;
	}



	public void setSpecialization_id(String specialization_id) {
		this.specialization_id = specialization_id;
	}



	public String getCurrent_semester() {
		return current_semester;
	}



	public void setCurrent_semester(String current_semester) {
		this.current_semester = current_semester;
	}



	public java.math.BigDecimal getCgpa() {
		return cgpa;
	}



	public void setCgpa(java.math.BigDecimal cgpa) {
		this.cgpa = cgpa;
	}



	public String getDivision() {
		return division;
	}



	public void setDivision(String division) {
		this.division = division;
	}



	public java.sql.Date getPassed_from_session() {
		return passed_from_session;
	}



	public void setPassed_from_session(java.sql.Date passed_from_session) {
		this.passed_from_session = passed_from_session;
	}



	


	


	
	
	
}
