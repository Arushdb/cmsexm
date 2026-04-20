package edu.dei.examination.phd.dto;

public class ProgramRoleDTO {
	
	Integer id;
	Integer userId ;
	 Integer programId;
	String username;
	String programname;
    String role;
	public ProgramRoleDTO(Integer id,Integer userId, Integer programId, String username, String programname, String role) {
		this.id=id;
		this.userId = userId;
		this.programId = programId;
		this.username = username;
		this.programname = programname;
		this.role = role;
	}
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getProgramId() { return programId; }
	public void setProgramId(Integer programId) { this.programId = programId; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getProgramname() { return programname; }
	public void setProgramname(String programname) { this.programname = programname; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	
	
	
	
	
    
    
    
}
