package edu.dei.examination.phd.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.dei.examination.phd.model.Scholars;

public class ScholarDTO {

	private Integer id;
	private String appno;
	private String fullName;
	private String category;

	private String email;
	private String phone;

	private int gender_id;
	private LocalDate dateOfBirth;
	private int programid;
	private String programname;
	private Integer department_id;
	private Integer userid;
	private Integer scholarid;
	private String enrolmentno;
	private String departmentName;
	private String programName;
	private String fathername;
	private String address;

	private LocalDate admissionDate;
	private LocalDate registrationdate;
	private LocalDate dateextension;
	private LocalDate dateJRFexp;
	private LocalDate dateJRF;

	private String researchtopiceng;
	private String researchtopichnd;
	private String nameinhindi;

	public ScholarDTO(String appno, String fullName, String category, String email, String phone, int gender_id,
			LocalDate dateOfBirth, int programid, LocalDate admissionDate, Integer department_id, Integer scholarid,
			String enrolmentno) {

		this.appno = appno;
		this.fullName = fullName;
		this.category = category;
		this.email = email;
		this.phone = phone;
		this.gender_id = gender_id;
		this.dateOfBirth = dateOfBirth;
		this.programid = programid;
		this.admissionDate = admissionDate;
		this.department_id = department_id;
		this.scholarid = scholarid;
		this.enrolmentno = enrolmentno;

	}

	public ScholarDTO() {
	}

	// DTO for full scholar detail
//	Integer, String, String, String, String, String, String, String, String, LocalDate, LocalDate, String, String, byte[], LocalDate, LocalDate, 
//	LocalDate, LocalDate) is undefined

	public ScholarDTO(Integer id, String fullName, String enrolmentno, String departmentName, String programName,
			String email, String phone, String fathername, String address, LocalDate admissionDate, LocalDate dateOfBirth,
			String researchtopiceng, String researchtopichnd, String nameinhindi, LocalDate dateJRF,
			LocalDate dateJRFexp, LocalDate dateextension, LocalDate registrationdate) {
		this.id = id;
		this.fullName = fullName;
		this.enrolmentno = enrolmentno;
		this.departmentName = departmentName;
		this.programname = programName;
		this.email = email;
		this.phone = phone;
		this.fathername = fathername;
		this.address = address;
		this.admissionDate = admissionDate;
		this.dateOfBirth = dateOfBirth;
		this.researchtopiceng = researchtopiceng;
		this.researchtopichnd = researchtopichnd;

		this.nameinhindi = nameinhindi;
		this.dateJRF = dateJRF;
		this.dateJRFexp = dateJRFexp;
		this.dateextension = dateextension;
		this.registrationdate = registrationdate;

	}

	// getters & setters


	public String getEmail() { return email; }

	public String getFullName() { return fullName; }

	public void setFullName(String fullName) { this.fullName = fullName; }

	

	public String getNameinhindi() { return nameinhindi; }

	public void setNameinhindi(String nameinhindi) { this.nameinhindi = nameinhindi; }

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

	

	public LocalDate getDateOfBirth() { return dateOfBirth; }

	public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

	public int getProgramid() { return programid; }

	public void setProgramid(int programid) { this.programid = programid; }

	public String getProgramname() { return programname; }

	public void setProgramname(String programname) { this.programname = programname; }

	public Integer getDepartment_id() { return department_id; }

	public void setDepartment_id(Integer department_id) { this.department_id = department_id; }

	public Integer getUserid() { return userid; }

	public void setUserid(Integer userid) { this.userid = userid; }

	public Integer getScholarid() { return scholarid; }

	public void setScholarid(Integer scholarid) { this.scholarid = scholarid; }

	public String getEnrolmentno() { return enrolmentno; }

	public void setEnrolmentno(String enrolmentno) { this.enrolmentno = enrolmentno; }

	public LocalDate getRegistrationdate() { return registrationdate; }

	public void setRegistrationdate(LocalDate registrationdate) { this.registrationdate = registrationdate; }

	public LocalDate getDateextension() { return dateextension; }

	public void setDateextension(LocalDate dateextension) { this.dateextension = dateextension; }

	public LocalDate getDateJRFexp() { return dateJRFexp; }

	public void setDateJRFexp(LocalDate dateJRFexp) { this.dateJRFexp = dateJRFexp; }

	public LocalDate getDateJRF() { return dateJRF; }

	public void setDateJRF(LocalDate dateJRF) { this.dateJRF = dateJRF; }

	public String getResearchtopiceng() { return researchtopiceng; }

	public void setResearchtopiceng(String researchtopiceng) { this.researchtopiceng = researchtopiceng; }

	public String getResearchtopichnd() { return researchtopichnd; }

	public void setResearchtopichnd(String researchtopichnd) { this.researchtopichnd = researchtopichnd; }

	public String getDepartmentName() { return departmentName; }

	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

	public String getProgramName() { return programName; }

	public void setProgramName(String programName) { this.programName = programName; }

	public String getFathername() { return fathername; }

	public void setFathername(String fathername) { this.fathername = fathername; }

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getAddress() { return address; }

	public void setAddress(String address) { this.address = address; }

}
