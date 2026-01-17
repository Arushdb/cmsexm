package edu.dei.examination.phd.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "semesters")
public class Semesters {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Column(name ="semester_id" )
	    private Integer semesterId;
	 @Column(name = "semester_name", length = 100)
	    private String semesterName;  // e.g. "Semester 1", "Odd 2025"

	 	@Column(name ="start_date" )
	    private LocalDate  semstartdate;
	 	@Column(name ="end_date" )
	    private LocalDate  semenddate;
	 	@Column(name ="reg_start_date" )
	    private LocalDate  regStartDate;
	 	@Column(name ="reg_end_date" )
	    private LocalDate  regEndDate;
	 	@Column(name ="is_active" )
	 	private Boolean active;
	 	
	 	 @Column(name = "academic_year", length = 10)
	     private String academicYear;  // e.g. "2025-26"

		public String getSemesterName() { return semesterName; }

		public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

		public LocalDate getSemstartdate() { return semstartdate; }

		public void setSemstartdate(LocalDate semstartdate) { this.semstartdate = semstartdate; }

		public LocalDate getSemenddate() { return semenddate; }

		public void setSemenddate(LocalDate semenddate) { this.semenddate = semenddate; }

		public LocalDate getRegStartDate() { return regStartDate; }

		public void setRegStartDate(LocalDate regStartDate) { this.regStartDate = regStartDate; }

		public LocalDate getRegEndDate() { return regEndDate; }

		public void setRegEndDate(LocalDate regEndDate) { this.regEndDate = regEndDate; }

		

	
		public Boolean getActive() { return active; }

		public void setActive(Boolean active) { this.active = active; }

		public void setSemesterId(Integer semesterId) { this.semesterId = semesterId; }

		public String getAcademicYear() { return academicYear; }

		public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

		public Integer getSemesterId() { return semesterId; }
		
		

				

		
	
	
	
}
