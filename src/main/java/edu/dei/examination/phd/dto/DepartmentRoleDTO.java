package edu.dei.examination.phd.dto;

public class DepartmentRoleDTO {
	
	Integer id ;
	Integer departmentid ;
	String departname;
	String username ;
	Integer userId;
	Integer facultyId ;
	String facultyName;
	String role;
	
	
	
	
	
	public DepartmentRoleDTO(Integer id, String username, Integer userId, Integer facultyId, String facultyName,
			String role) {
		
		this.id = id;
		this.username = username;
		this.userId = userId;
		this.facultyId = facultyId;
		this.facultyName = facultyName;
		this.role = role;
	}



	public DepartmentRoleDTO(Integer id, Integer departmentid, String departname, String username, Integer userId) {
		
		this.id = id;
		this.departmentid = departmentid;
		this.departname = departname;
		this.username = username;
		this.userId = userId;
	}
	
	
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getDepartmentid() { return departmentid; }
	public void setDepartmentid(Integer departmentid) { this.departmentid = departmentid; }
	public String getDepartname() { return departname; }
	public void setDepartname(String departname) { this.departname = departname; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }



	public Integer getFacultyId() { return facultyId; }



	public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }



	public String getFacultyName() { return facultyName; }



	public void setFacultyName(String facultyName) { this.facultyName = facultyName; }



	public String getRole() { return role; }



	public void setRole(String role) { this.role = role; }
	
	
	
	

}
