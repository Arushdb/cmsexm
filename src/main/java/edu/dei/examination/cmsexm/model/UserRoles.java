package edu.dei.examination.cmsexm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

//@Entity
//@Table(name="user_roles")
//public class UserRoles {
//
//	@EmbeddedId
//    private UserRolesPK userrolePK;
//	
//	private boolean default_role;
//
//	
////	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
////	@JoinColumn(name = "userrolePK" ,insertable = false,updatable = false)
////	@JsonBackReference
////	
////	private User user;
////	
//	
//	public UserRoles() {
//		
//	}
//
//	
//	
//	
//	public UserRoles(boolean default_role) {
//	
//		this.default_role = default_role;
//	}
//
//	public UserRolesPK getUserrolePK() {
//		return userrolePK;
//	}
//
//	public void setUserrolePK(UserRolesPK userrolePK) {
//		this.userrolePK = userrolePK;
//	}
//
//	public boolean isDefault_role() {
//		return default_role;
//	}
//
//	public void setDefault_role(boolean default_role) {
//		this.default_role = default_role;
//	}
//	
//	
//	
//}


@Entity
@Table(name="exam_live.user_roles",schema = "exam_live")
public class UserRoles {

    @EmbeddedId
    private UserRolesPK userrolePK;

    private boolean default_role;

    // 🔥 IMPORTANT: map user
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("id")   // field name inside UserRolesPK
//    @JoinColumn(name = "user_id")
//    private User user;
//    
//    @ManyToOne
//    @MapsId("roleId")
//    @JoinColumn(name = "role_id")
//    private Role role;
    
    
    

    public UserRoles() {}

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

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}