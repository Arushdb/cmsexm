package edu.dei.examination.phd.dto;


public class SupervisorAssignmentDTO {

    private Integer id;
    private Integer scholarId;
    private String scholarName;
    private String enrollmentNo;

    private Integer supervisorId;
    private Integer userId;
    
    private String supervisorName;
    
    
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getScholarId() { return scholarId; }
	public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }
	public String getScholarName() { return scholarName; }
	public void setScholarName(String scholarName) { this.scholarName = scholarName; }
	public String getEnrollmentNo() { return enrollmentNo; }
	public void setEnrollmentNo(String enrollmentNo) { this.enrollmentNo = enrollmentNo; }
	public Integer getSupervisorId() { return supervisorId; }
	public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }
	public String getSupervisorName() { return supervisorName; }
	public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	

    // getters & setters
    
    
}