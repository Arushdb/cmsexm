package edu.dei.examination.phd.model;



import javax.persistence.*;

import edu.dei.examination.cmsexm.model.User;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "faculty_role_assignment",
    uniqueConstraints = {
        // 🔥 Only ONE Dean per faculty
        @UniqueConstraint(columnNames = {"faculty_id", "role"})
    }
)
public class FacultyRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // =========================
    // FACULTY
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    // =========================
    // USER (DEAN)
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =========================
    // ROLE
    // =========================
    @Column(nullable = false, length = 50)
    private String role; // "DEAN"

   

    // =========================
    // CONSTRUCTORS
    // =========================
    public FacultyRoleAssignment() {}

    public FacultyRoleAssignment(Faculty faculty, User user, String role) {
        this.faculty = faculty;
        this.user = user;
        this.role = role;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

  
}