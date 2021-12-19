package edu.dei.examination.cmsexm.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "student_master",schema ="cms_live_local" ,catalog ="cms_live_local" )


public class StudentMaster {
	
	@Id
	private String enrollment_number;
	

	private String student_first_name;
	
	
	public StudentMaster() {
		
	}
	
	public StudentMaster(String student_first_name) {
	
		this.student_first_name = student_first_name;
	}

	public String getEnrollment_number() {
		return enrollment_number;
	}


	public void setEnrollment_number(String enrollment_number) {
		this.enrollment_number = enrollment_number;
	}


	public String getStudent_first_name() {
		return student_first_name;
	}


	public void setStudent_first_name(String student_first_name) {
		this.student_first_name = student_first_name;
	}






	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enrollment_number == null) ? 0 : enrollment_number.hashCode());
		result = prime * result + ((student_first_name == null) ? 0 : student_first_name.hashCode());
		return result;
	}






	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentMaster other = (StudentMaster) obj;
		if (enrollment_number == null) {
			if (other.enrollment_number != null)
				return false;
		} else if (!enrollment_number.equals(other.enrollment_number))
			return false;
		if (student_first_name == null) {
			if (other.student_first_name != null)
				return false;
		} else if (!student_first_name.equals(other.student_first_name))
			return false;
		return true;
	}
		
	
	
	
	
}
