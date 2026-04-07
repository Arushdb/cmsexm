package edu.dei.examination.phd.model;

import javax.persistence.*;

import edu.dei.examination.phd.enums.SupervisorRole;

import java.time.LocalDate;

@Entity
@Table(name = "scholar_supervisors")
public class ScholarSupervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "scholar_id", nullable = false)
//    private Integer scholarId;

    @Column(name = "supervisor_id", nullable = false)
    private Integer supervisorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private SupervisorRole role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "assigned_on")
    private LocalDate assignedOn = LocalDate.now();

    public Integer getId() { return id; }
    
    @ManyToOne
    @JoinColumn(name = "scholar_id")
    private Scholars scholar;

//    public Integer getScholarId() { return scholarId; }
//    public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }

    public Integer getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }

    public SupervisorRole getRole() { return role; }
    public void setRole(SupervisorRole role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDate getAssignedOn() { return assignedOn; }
    public void setAssignedOn(LocalDate assignedOn) { this.assignedOn = assignedOn; }
	public Scholars getScholar() { return scholar; }
	public void setScholar(Scholars scholar) { this.scholar = scholar; }
    
    
}