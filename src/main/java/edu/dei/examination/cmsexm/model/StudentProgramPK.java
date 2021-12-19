package edu.dei.examination.cmsexm.model;

import java.io.Serializable;


public class StudentProgramPK  implements Serializable {
	
	private String enrollment_number;
	private String roll_number;
	
	private String program_status;
	private String entity_id;
	private String program_id;
	private String branch_id;
	private String specialization_id;
	
	public StudentProgramPK(String enrollment_number, String roll_number, String program_status, String entity_id,
			String program_id, String branch_id, String specialization_id) {
	
		this.enrollment_number = enrollment_number;
		this.roll_number = roll_number;
		this.program_status = program_status;
		this.entity_id = entity_id;
		this.program_id = program_id;
		this.branch_id = branch_id;
		this.specialization_id = specialization_id;
	}

	
	
	
	public StudentProgramPK() {
		
	}




	public String getEnrollment_number() {
		return enrollment_number;
	}

	public void setEnrollment_number(String enrollment_number) {
		this.enrollment_number = enrollment_number;
	}

	public String getRoll_number() {
		return roll_number;
	}

	public void setRoll_number(String roll_number) {
		this.roll_number = roll_number;
	}

	public String getProgram_status() {
		return program_status;
	}

	public void setProgram_status(String program_status) {
		this.program_status = program_status;
	}

	public String getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getSpecialization_id() {
		return specialization_id;
	}

	public void setSpecialization_id(String specialization_id) {
		this.specialization_id = specialization_id;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch_id == null) ? 0 : branch_id.hashCode());
		result = prime * result + ((enrollment_number == null) ? 0 : enrollment_number.hashCode());
		result = prime * result + ((entity_id == null) ? 0 : entity_id.hashCode());
		result = prime * result + ((program_id == null) ? 0 : program_id.hashCode());
		result = prime * result + ((program_status == null) ? 0 : program_status.hashCode());
		result = prime * result + ((roll_number == null) ? 0 : roll_number.hashCode());
		result = prime * result + ((specialization_id == null) ? 0 : specialization_id.hashCode());
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
		StudentProgramPK other = (StudentProgramPK) obj;
		if (branch_id == null) {
			if (other.branch_id != null)
				return false;
		} else if (!branch_id.equals(other.branch_id))
			return false;
		if (enrollment_number == null) {
			if (other.enrollment_number != null)
				return false;
		} else if (!enrollment_number.equals(other.enrollment_number))
			return false;
		if (entity_id == null) {
			if (other.entity_id != null)
				return false;
		} else if (!entity_id.equals(other.entity_id))
			return false;
		if (program_id == null) {
			if (other.program_id != null)
				return false;
		} else if (!program_id.equals(other.program_id))
			return false;
		if (program_status == null) {
			if (other.program_status != null)
				return false;
		} else if (!program_status.equals(other.program_status))
			return false;
		if (roll_number == null) {
			if (other.roll_number != null)
				return false;
		} else if (!roll_number.equals(other.roll_number))
			return false;
		if (specialization_id == null) {
			if (other.specialization_id != null)
				return false;
		} else if (!specialization_id.equals(other.specialization_id))
			return false;
		return true;
	}

	
	
	
}
