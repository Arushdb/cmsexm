package edu.dei.examination.phd.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String code;
    
    @JsonIgnore 
    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<FacultyRoleAssignment> roleAssignments;

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getCode() { return code; }

	public void setCode(String code) { this.code = code; }

	public Faculty(Integer id, String name) {
		
		this.id = id;
		this.name = name;
	}

	public Faculty() {
		
		// TODO Auto-generated constructor stub
	}
	
	

    // getters & setters
    
    
}