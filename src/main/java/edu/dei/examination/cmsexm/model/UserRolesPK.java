package edu.dei.examination.cmsexm.model;

import java.io.Serializable;
import java.util.Objects;

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
	
	
	 // 🔥 VERY IMPORTANT
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRolesPK)) return false;
        UserRolesPK that = (UserRolesPK) o;
        return Objects.equals(user_id, that.user_id) &&
               Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role_id, role_id);
    }
	

}


