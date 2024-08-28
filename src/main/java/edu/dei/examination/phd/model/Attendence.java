package edu.dei.examination.phd.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "attendence",schema ="phd" ,catalog ="phd" )


public class Attendence {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	private String periodfm;
	private String periodto;
	private float presence;
	public String getPeriodfm() {
		return periodfm;
	}
	public void setPeriodfm(String periodfm) {
		this.periodfm = periodfm;
	}
	public String getPeriodto() {
		return periodto;
	}
	public void setPeriodto(String periodto) {
		this.periodto = periodto;
	}
	public float getPresence() {
		return presence;
	}
	public void setPresence(float presence) {
		this.presence = presence;
	}
	
	
	
	

	}
