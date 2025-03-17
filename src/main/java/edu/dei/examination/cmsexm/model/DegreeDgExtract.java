package edu.dei.examination.cmsexm.model;

//   Programmer : Pragya
//     Created on :


import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dg_extract", schema = "exam_live", catalog = "exam_live")
public class DegreeDgExtract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String org_name;
 
    private String academic_course_id;
    private String course_name;
    private String sub_course_name;
    private String regn_no;
    private String rroll;
    private String cname;
    private String gender;
    private String dob;
    private String mrks_rec_status;
    private String year;
    private String month;
    private String division;
    private String grade;
    private String doi;
    private String cgpa;
    private String abc_account_id;
    private String sub1nm;
    private String AADHAAR_NAME;
    private String DIVISION_TH;
    private String DIVISION_PR;
    private int dg_controller_id;

    public DegreeDgExtract() {
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	

	public String getAcademic_course_id() {
		return academic_course_id;
	}

	public void setAcademic_course_id(String academic_course_id) {
		this.academic_course_id = academic_course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	

	public String getRegn_no() {
		return regn_no;
	}

	public void setRegn_no(String regn_no) {
		this.regn_no = regn_no;
	}

	public String getRroll() {
		return rroll;
	}

	public void setRroll(String rroll) {
		this.rroll = rroll;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	
	public String getMrks_rec_status() {
		return mrks_rec_status;
	}

	public void setMrks_rec_status(String mrks_rec_status) {
		this.mrks_rec_status = mrks_rec_status;
	}

	

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	

	public String getCgpa() {
		return cgpa;
	}

	public void setCgpa(String cgpa) {
		this.cgpa = cgpa;
	}


	public String getAbc_account_id() {
		return abc_account_id;
	}

	public void setAbc_account_id(String abc_account_id) {
		this.abc_account_id = abc_account_id;
	}

	
	
	public String getAADHAAR_NAME() {
		return AADHAAR_NAME;
	}

	public void setAADHAAR_NAME(String aADHAAR_NAME) {
		AADHAAR_NAME = aADHAAR_NAME;
	}

	

	public int getDg_controller_id() {
		return dg_controller_id;
	}

	public void setDg_controller_id(int dg_controller_id) {
		this.dg_controller_id = dg_controller_id;
	}

	public String getSub1nm() {
		return sub1nm;
	}

	public void setSub1nm(String sub1nm) {
		this.sub1nm = sub1nm;
	}

	public String getDIVISION_TH() {
		return DIVISION_TH;
	}

	public void setDIVISION_TH(String dIVISION_TH) {
		DIVISION_TH = dIVISION_TH;
	}

	public String getDIVISION_PR() {
		return DIVISION_PR;
	}

	public void setDIVISION_PR(String dIVISION_PR) {
		DIVISION_PR = dIVISION_PR;
	}

	public String getSub_course_name() {
		return sub_course_name;
	}

	public void setSub_course_name(String sub_course_name) {
		this.sub_course_name = sub_course_name;
	}

 	
}
