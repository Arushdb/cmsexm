package edu.dei.examination.cmsexm.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Embeddable
public class UserRolesPK implements Serializable {
	
	private int user_id;
	private int role_id;
		
	public UserRolesPK(int user_id, int role_id) {
	
		this.user_id = user_id;
		this.role_id = role_id;
	}

	public UserRolesPK() {
	
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public int getRole_id() {
		return role_id;
	}


	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	
	
	
	

}


