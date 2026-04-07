package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "program_role_assignment")
public class ProgramRoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    

    // Constructors
    public ProgramRoleAssignment() {}

	public Program getProgram() { return program; }

	public void setProgram(Program program) { this.program = program; }

	public Integer getUserId() { return userId; }

	public void setUserId(Integer userId) { this.userId = userId; }

	public String getRole() { return role; }

	public void setRole(String role) { this.role = role; }

	public Boolean getIsActive() { return isActive; }

	public void setIsActive(Boolean isActive) { this.isActive = isActive; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getters & Setters
    
    
    
}
