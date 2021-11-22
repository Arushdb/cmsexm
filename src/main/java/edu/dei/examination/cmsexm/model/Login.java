package edu.dei.examination.cmsexm.model;

public class Login {
	
	 private String username;
	 private String password;
	 private String role;
	 
	 

	private int menuId ;
	 
		 
	public Login(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	
	
	public Login() {
	
		// TODO Auto-generated constructor stub
	}



	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}



	public int getMenuId() {
		return menuId;
	}



	public void setMenuId(int menuId) {
		this.menuId = menuId;
	} 
	
	 
	 

}
