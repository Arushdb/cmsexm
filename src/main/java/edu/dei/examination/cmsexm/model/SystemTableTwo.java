package edu.dei.examination.cmsexm.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "system_table_two", schema = "cms_live_local", catalog = "cms_live_local")

public class SystemTableTwo {

	@Id
	private String component_code;

	private String component_description;

	private String group_code;

	public String getGroup_code() {
		return group_code;
	}

	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}

	public SystemTableTwo() {

	}

	public String getComponent_code() {
		return component_code;
	}

	public void setComponent_code(String component_code) {
		this.component_code = component_code;
	}

	public String getComponent_description() {
		return component_description;
	}

	public void setComponent_description(String component_description) {
		this.component_description = component_description;
	}

}
