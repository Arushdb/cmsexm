package edu.dei.examination.cmsexm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="user_roles")
public class UserRoles {

	@EmbeddedId
    private UserRolesPK userrolePK;
	
	private boolean default_role;

	
//	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
//	@JoinColumn(name = "userrolePK" ,insertable = false,updatable = false)
//	@JsonBackReference
//	
//	private User user;
//	
	
	public UserRoles() {
		
	}

	
	
	
	public UserRoles(boolean default_role) {
	
		this.default_role = default_role;
	}

	public UserRolesPK getUserrolePK() {
		return userrolePK;
	}

	public void setUserrolePK(UserRolesPK userrolePK) {
		this.userrolePK = userrolePK;
	}

	public boolean isDefault_role() {
		return default_role;
	}

	public void setDefault_role(boolean default_role) {
		this.default_role = default_role;
	}
	
	
	
}
