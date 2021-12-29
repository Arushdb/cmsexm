package edu.dei.examination.cmsexm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity()
@Table(name = "ver_references")



public class VerificationAgencyReferences {
	
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="agency_id")
	private int agencyid;
	
	
	private String  contact_number;
	private String  email;
	private String  reference_no;
	


	private String  request_mode;
	
	@Column(name="request_received_date" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  request_received_date;
	
	@Column(name="process_status")
	private String  processstatus;
	private String  remarks;
	
	@Column(name="generated_date" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  generated_date;
	
	
	@Column(name="insert_time" ,nullable = false, updatable = false ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date  insert_time;
	
	@Column(name="modification_time" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date  modification_time;
	
	
	private String  creator_id;
	
	private String  modifier_id;
	
//	@ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
//			 CascadeType.DETACH, CascadeType.REFRESH})
//	@JoinColumn(name = "agency_id")
//	@JsonBackReference
//	private VerificationAgency requester;
	
	public VerificationAgencyReferences() {
	
	}

	
//	@ManyToMany(fetch=FetchType.LAZY,
//			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
//			 CascadeType.DETACH, CascadeType.REFRESH})
//	@JoinTable(
//			name="ver_ref_enrolmentno",
//			joinColumns=@JoinColumn(name="reference_id"),
//			inverseJoinColumns=@JoinColumn(name="enrolment_id")
//			)
	
	
//	@OneToMany(fetch=FetchType.LAZY,
//			   mappedBy="myreference",
//			   cascade= {CascadeType.PERSIST, CascadeType.MERGE,
//						 CascadeType.DETACH, CascadeType.REFRESH})
//	@JsonManagedReference
//	private List<VerificationEnrolmentno>   enrolmentno;
	
	@OneToMany(fetch=FetchType.LAZY,
			   mappedBy="rollnoreference",
			   cascade= {CascadeType.PERSIST, CascadeType.MERGE,
						 CascadeType.DETACH, CascadeType.REFRESH})
	@JsonManagedReference
	private List<VerificationRollno>   rollno;
	
		


	public VerificationAgencyReferences(int agencyid, String contact_number, String email, String reference_no,
		String request_mode, Date request_received_date, String processstatus, String remarks, Date generated_date,
		Date insert_time, Date modification_time, String creator_id, String modifier_id,
		List<VerificationRollno> rollno) {

	this.agencyid = agencyid;
	this.contact_number = contact_number;
	this.email = email;
	this.reference_no = reference_no;
	this.request_mode = request_mode;
	this.request_received_date = request_received_date;
	this.processstatus = processstatus;
	this.remarks = remarks;
	this.generated_date = generated_date;
	this.insert_time = insert_time;
	this.modification_time = modification_time;
	this.creator_id = creator_id;
	this.modifier_id = modifier_id;
	this.rollno = rollno;
}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}

	public String getRequest_mode() {
		return request_mode;
	}

	public void setRequest_mode(String request_mode) {
		this.request_mode = request_mode;
	}

	

	public Date getRequest_received_date() {
		return request_received_date;
	}

	public void setRequest_received_date(Date request_received_date) {
		this.request_received_date = request_received_date;
	}



	public String getProcessstatus() {
		return processstatus;
	}

	public void setProcessstatus(String processstatus) {
		this.processstatus = processstatus;
	}

	public Date getGenerated_date() {
		return generated_date;
	}

	public void setGenerated_date(Date generated_date) {
		this.generated_date = generated_date;
	}

	public Date getInsert_time() {
		return insert_time;
	}

	public void setInsert_time(Date insert_time) {
		this.insert_time = insert_time;
	}

	public Date getModification_time() {
		return modification_time;
	}

	public void setModification_time(Date modification_time) {
		this.modification_time = modification_time;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
	

	public String getModifier_id() {
		return modifier_id;
	}

	
	public void setModifier_id(String modifier_id) {
		this.modifier_id = modifier_id;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	


	public int getAgencyid() {
		return agencyid;
	}

	public void setAgencyid(int agencyid) {
		this.agencyid = agencyid;
	}

	public List<VerificationRollno> getRollno() {
		return rollno;
	}

	public void setRollno(List<VerificationRollno> rollno) {
		this.rollno = rollno;
	}

	


	}
