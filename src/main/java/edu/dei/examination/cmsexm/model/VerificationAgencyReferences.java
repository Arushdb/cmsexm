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
	
	//@Column(insertable = false,updatable = false)
	private int agency_id;
	
	
	private String  contact_number;
	private String  email;
	private String  reference_no;
	


	private String  request_mode;
	
	@Column(name="request_received_date" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  reqrcvdate;
	
	private String  process_status;
	private String  remarks;
	
	@Column(name="generated_date" ,columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  gen_date;
	
	
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
	
	
	@OneToMany(fetch=FetchType.LAZY,
			   mappedBy="myreference",
			   cascade= {CascadeType.PERSIST, CascadeType.MERGE,
						 CascadeType.DETACH, CascadeType.REFRESH})
	@JsonManagedReference
	private List<VerificationEnrolmentno>   enrolmentno;
	
	
	

	public VerificationAgencyReferences(int agency_id, String contact_number, String email, String reference_no,
			String request_mode, Date reqrcvdate, String process_status, String remarks, Date gen_date,
			Date insert_time, Date modification_time, String creator_id, String modifier_id) {
		super();
		this.agency_id = agency_id;
		this.contact_number = contact_number;
		this.email = email;
		this.reference_no = reference_no;
		this.request_mode = request_mode;
		this.reqrcvdate = reqrcvdate;
		this.process_status = process_status;
		this.remarks = remarks;
		this.gen_date = gen_date;
		this.insert_time = insert_time;
		this.modification_time = modification_time;
		this.creator_id = creator_id;
		this.modifier_id = modifier_id;
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

	public Date getReqrcvdate() {
		return reqrcvdate;
	}

	public void setReqrcvdate(Date reqrcvdate) {
		this.reqrcvdate = reqrcvdate;
	}

	public String getProcess_status() {
		return process_status;
	}

	public void setProcess_status(String process_status) {
		this.process_status = process_status;
	}

	public Date getGen_date() {
		return gen_date;
	}

	public void setGen_date(Date gen_date) {
		this.gen_date = gen_date;
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

	
	

	
//	public List<Ver_enrolmentno> getEnrolmentno() {
//		return enrolmentno;
//	}
//
//	public void setEnrolmentno(List<Ver_enrolmentno> enrolmentno) {
//		this.enrolmentno = enrolmentno;
//	}





	public int getAgency_id() {
		return agency_id;
	}





	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}


	}
