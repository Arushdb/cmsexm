package edu.dei.examination.phd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "supervisors")
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supervisor_id")
    private Integer supervisorId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name="full_name")
    private String name;
    
    @Column(name = "department_id")
    private Integer department;

	public Integer getSupervisorId() { return supervisorId; }

	public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }

	public Integer getUserId() { return userId; }

	public void setUserId(Integer userId) { this.userId = userId; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public Integer getDepartment() { return department; }

	public void setDepartment(Integer department) { this.department = department; }

	

    // getters & setters
    
    
}
