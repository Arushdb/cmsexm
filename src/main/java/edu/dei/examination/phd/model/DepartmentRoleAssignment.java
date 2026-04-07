package edu.dei.examination.phd.model;



import javax.persistence.*;

@Entity
@Table(name = "department_role_assignment")
public class DepartmentRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "department_id", nullable = false,insertable = false,updatable = false)
//    private Integer departmentId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;   // HOD

    @Column(name = "user_id", nullable = false)
    private Integer userId;
    private String role;        // 🔥 ADD THIS
    private Boolean isActive;   // 🔥 ADD THIS
    
    
    // 🔗 Department Mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /* ---------- Lifecycle ---------- */

   

    /* ---------- Getters & Setters ---------- */

    public Integer getId() { return id; }

//    public Integer getDepartmentId() { return departmentId; }
//    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

	public Department getDepartment() { return department; }

	public void setDepartment(Department department) { this.department = department; }

	public String getRole() { return role; }

	public void setRole(String role) { this.role = role; }

	public Boolean getIsActive() { return isActive; }

	public void setIsActive(Boolean isActive) { this.isActive = isActive; }
	
	

    }