package edu.dei.examination.cmsexm.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ver_enrolmentno", uniqueConstraints = @UniqueConstraint(columnNames = "enrolmentno"))
public class VerificationEnrolmentno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int enrolmentno;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "reference_id")
	@JsonBackReference
	private VerificationAgencyReferences myreference;
	
	
	


//	@ManyToMany(fetch=FetchType.LAZY,
//			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
//			 CascadeType.DETACH, CascadeType.REFRESH})
//	@JoinTable(
//			name="ver_ref_enrolmentno",
//			joinColumns=@JoinColumn(name="enrolment_id"),
//			inverseJoinColumns=@JoinColumn(name="reference_id")
//			)
//	private List<VerificationAgencyReferences>   reference;

	public VerificationEnrolmentno(int enrolmentno) {

		this.enrolmentno = enrolmentno;
	}

	public VerificationEnrolmentno() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VerificationAgencyReferences getMyreference() {
		return myreference;
	}

	public void setMyreference(VerificationAgencyReferences myreference) {
		this.myreference = myreference;
	}

	public int getEnrolmentno() {
		return enrolmentno;
	}

	public void setEnrolmentno(int enrolmentno) {
		this.enrolmentno = enrolmentno;
	}

	@Override
	public String toString() {
		return "Ver_enrolmentno [id=" + id + ", myreference=" + myreference + "]";
	}

	

}
