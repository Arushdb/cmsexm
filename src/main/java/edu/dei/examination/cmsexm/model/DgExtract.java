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
public class DgExtract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String org_name;
    private String org_name_l;
    private String academic_course_id;
    private String course_name;
    private String course_name_l;
    private String stream;
    private String stream_l;
    private String session;
    private String regn_no;
    private String rroll;
    private String cname;
    private String gender;
    private String dob;
    private String fname;
    private String mname;
    private String photo;
    private String mrks_rec_status;
    private String result;
    private String year;
    private String month;
    private String division;
    private String grade;
    private String percent;
    private String doi;
    private String sem;
    private String exam_type;
    private String tot;
    private String tot_mrks;
    private String tot_credit;
    private String tot_credit_points;
    private String tot_grade_points;
    private String grand_tot_max;
    private String grand_tot_mrks;
    private String grand_tot_credit_points;
    private String cgpa;
    private String remarks;
    private String sgpa;
    private String abc_account_id;
    private String term_type;
    private String tot_grade;
    private String subjects_data;
    private Timestamp insert_time;
    private String AADHAAR_NAME;
    private String ADMISSION_YEAR;
    private int dg_controller_id;

    public DgExtract() {
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

	public String getOrg_name_l() {
		return org_name_l;
	}

	public void setOrg_name_l(String org_name_l) {
		this.org_name_l = org_name_l;
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

	public String getCourse_name_l() {
		return course_name_l;
	}

	public void setCourse_name_l(String course_name_l) {
		this.course_name_l = course_name_l;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getStream_l() {
		return stream_l;
	}

	public void setStream_l(String stream_l) {
		this.stream_l = stream_l;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
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

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMrks_rec_status() {
		return mrks_rec_status;
	}

	public void setMrks_rec_status(String mrks_rec_status) {
		this.mrks_rec_status = mrks_rec_status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getSem() {
		return sem;
	}

	public void setSem(String sem) {
		this.sem = sem;
	}

	public String getExam_type() {
		return exam_type;
	}

	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}

	public String getTot() {
		return tot;
	}

	public void setTot(String tot) {
		this.tot = tot;
	}

	public String getTot_mrks() {
		return tot_mrks;
	}

	public void setTot_mrks(String tot_mrks) {
		this.tot_mrks = tot_mrks;
	}

	public String getTot_credit() {
		return tot_credit;
	}

	public void setTot_credit(String tot_credit) {
		this.tot_credit = tot_credit;
	}

	public String getTot_credit_points() {
		return tot_credit_points;
	}

	public void setTot_credit_points(String tot_credit_points) {
		this.tot_credit_points = tot_credit_points;
	}

	public String getTot_grade_points() {
		return tot_grade_points;
	}

	public void setTot_grade_points(String tot_grade_points) {
		this.tot_grade_points = tot_grade_points;
	}

	public String getGrand_tot_max() {
		return grand_tot_max;
	}

	public void setGrand_tot_max(String grand_tot_max) {
		this.grand_tot_max = grand_tot_max;
	}

	public String getGrand_tot_mrks() {
		return grand_tot_mrks;
	}

	public void setGrand_tot_mrks(String grand_tot_mrks) {
		this.grand_tot_mrks = grand_tot_mrks;
	}

	public String getGrand_tot_credit_points() {
		return grand_tot_credit_points;
	}

	public void setGrand_tot_credit_points(String grand_tot_credit_points) {
		this.grand_tot_credit_points = grand_tot_credit_points;
	}

	public String getCgpa() {
		return cgpa;
	}

	public void setCgpa(String cgpa) {
		this.cgpa = cgpa;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSgpa() {
		return sgpa;
	}

	public void setSgpa(String sgpa) {
		this.sgpa = sgpa;
	}

	public String getAbc_account_id() {
		return abc_account_id;
	}

	public void setAbc_account_id(String abc_account_id) {
		this.abc_account_id = abc_account_id;
	}

	public String getTerm_type() {
		return term_type;
	}

	public void setTerm_type(String term_type) {
		this.term_type = term_type;
	}

	public String getTot_grade() {
		return tot_grade;
	}

	public void setTot_grade(String tot_grade) {
		this.tot_grade = tot_grade;
	}

	public String getSubjects_data() {
		return subjects_data;
	}

	public void setSubjects_data(String subjects_data) {
		this.subjects_data = subjects_data;
	}

	public Timestamp getInsert_time() {
		return insert_time;
	}

	public void setInsert_time(Timestamp insert_time) {
		this.insert_time = insert_time;
	}

	public String getAADHAAR_NAME() {
		return AADHAAR_NAME;
	}

	public void setAADHAAR_NAME(String aADHAAR_NAME) {
		AADHAAR_NAME = aADHAAR_NAME;
	}

	public String getADMISSION_YEAR() {
		return ADMISSION_YEAR;
	}

	public void setADMISSION_YEAR(String aDMISSION_YEAR) {
		ADMISSION_YEAR = aDMISSION_YEAR;
	}

	public int getDg_controller_id() {
		return dg_controller_id;
	}

	public void setDg_controller_id(int dg_controller_id) {
		this.dg_controller_id = dg_controller_id;
	}

 	
}
