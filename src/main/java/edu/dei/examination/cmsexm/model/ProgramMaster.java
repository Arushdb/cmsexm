package edu.dei.examination.cmsexm.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "program_master",schema ="cms_live_local" ,catalog ="cms_live_local" )


public class ProgramMaster {
	
	@Id
	private String program_id;
	

	private String program_name;
	
	
	public ProgramMaster() {
		
	}

	

	public String getProgram_id() {
		return program_id;
	}


	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}


	public String getProgram_name() {
		return program_name;
	}


	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	
		
	
	
	
}
