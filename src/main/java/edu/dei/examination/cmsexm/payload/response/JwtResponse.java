package edu.dei.examination.cmsexm.payload.response;

import java.util.List;

import org.json.JSONArray;

public class JwtResponse {
	
	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	
	private String menuary ;
	private List<String> roles;

	public JwtResponse(String accessToken, Integer id, String username,  List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
	
		this.roles = roles;
	}

	
	
	
	public JwtResponse(String token, Integer id, String username, String menuary, List<String> roles) {
	
		this.token = token;
		this.id = id;
		this.username = username;
		this.menuary = menuary;
		this.roles = roles;
	}




	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	



	public Integer getId() { return id; }




	public void setId(Integer id) { this.id = id; }




	public void setRoles(List<String> roles) { this.roles = roles; }




	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getMenuary() {
		return menuary;
	}

	public void setMenuary(String menuary) {
		this.menuary = menuary;
	}

}
