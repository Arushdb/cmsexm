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
@Table(name = "ver_rollnumber", uniqueConstraints = @UniqueConstraint(columnNames = "rollno"))
public class VerificationRollno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	private int rollno;


		
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "reference_id")
	@JsonBackReference
	private VerificationAgencyReferences rollnoreference;

	
	public VerificationRollno() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	


	public VerificationRollno(int rollno) {
	
		this.rollno = rollno;
	}

	public int getRollno() {
		return rollno;
	}

	public void setRollno(int rollno) {
		this.rollno = rollno;
	}

	public VerificationAgencyReferences getRollnoreference() {
		return rollnoreference;
	}

	public void setRollnoreference(VerificationAgencyReferences rollnoreference) {
		this.rollnoreference = rollnoreference;
	}

	

	

}
