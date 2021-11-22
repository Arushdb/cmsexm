package edu.dei.examination.cmsexm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "role_menu")
public class Role_Menu {

   @Id	
  int role_id;
  int menu_id;
  
  
public Role_Menu() {
	super();
	// TODO Auto-generated constructor stub
}

public int getRole_id() {
	return role_id;
}
public void setRole_id(int role_id) {
	this.role_id = role_id;
}
public int getMenu_id() {
	return menu_id;
}
public void setMenu_id(int menu_id) {
	this.menu_id = menu_id;
}
  
  

}
