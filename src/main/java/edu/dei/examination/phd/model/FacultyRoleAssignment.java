package edu.dei.examination.phd.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "faculty_role_assignment")
public class FacultyRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "faculty_id", nullable = false)
    private Integer facultyId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;   // DEAN

    @Column(name = "user_id", nullable = false)
    private Integer userId;

   
    /* ---------- Lifecycle ---------- */

      /* ---------- Getters & Setters ---------- */

    public Integer getId() { return id; }

    public Integer getFacultyId() { return facultyId; }
    public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    
}