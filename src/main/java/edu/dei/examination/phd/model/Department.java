package edu.dei.examination.phd.model;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity

@Table(	name = "departments", 
uniqueConstraints = { 
	@UniqueConstraint(columnNames = { "code","name"})

})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    // 🔤 Department Name
    @Column(name = "name", nullable = false, length = 150)
    private String departmentName;

    // 🔑 Short Code (e.g., CSE, ECE)
    @Column(name = "code", nullable = false, length = 20)
    private String departmentCode;

//    // 🏫 Faculty / School Name (optional)
//    @Column(name = "faculty_id")
//    private String facultyid;
    
    // =========================
    // FACULTY RELATION
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnore
    private Faculty faculty;

   

    // ================= RELATIONSHIPS =================

    // 🔗 One Department → Many Scholars
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Scholars> scholars;

    // 🔗 One Department → Many Role Assignments (Supervisor/HOD)
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DepartmentRoleAssignment> roleAssignments;

    // ================= CONSTRUCTORS =================

    public Department() {}

    public Department(String departmentName, String departmentCode) {
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
    }

    // ================= GETTERS & SETTERS =================

   

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

   

  

	public Faculty getFaculty() { return faculty; }

	public void setFaculty(Faculty faculty) { this.faculty = faculty; }

	

	public void setScholars(List<Scholars> scholars) { this.scholars = scholars; }

	public void setRoleAssignments(List<DepartmentRoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}

	

    public List<Scholars> getScholars() {
        return scholars;
    }

    public List<DepartmentRoleAssignment> getRoleAssignments() {
        return roleAssignments;
    }

	public Integer getDepartmentId() { return departmentId; }

	public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    
    
}