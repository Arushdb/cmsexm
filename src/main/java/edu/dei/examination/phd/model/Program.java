package edu.dei.examination.phd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "programs")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "name", nullable = false, length = 100)
    private String programName;

    @Column(name = "mode")
    private String mode;
    
    @Column(name = "department_id")
    private Integer departmentid;

    @Column(name = "min_duration_months")
    private Integer mindurationYears;
    
    @Column(name = "max_duration_months")
    private Integer maxdurationYears;

    // 🔥 Reverse relationship (optional but recommended)
    @JsonIgnore
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<Scholars> scholars;

    // ===== Constructors =====

    public Program() {
    }

       // ===== Getters & Setters =====

   

    public String getProgramName() {
        return programName;
    }

   

	public Long getProgramId() { return programId; }

	public void setProgramId(Long programId) { this.programId = programId; }

	public void setProgramName(String programName) {
        this.programName = programName;
    }



	public String getMode() { return mode; }
	public void setMode(String mode) { this.mode = mode; }

	public Integer getDepartmentid() { return departmentid; }

	public void setDepartmentid(Integer departmentid) { this.departmentid = departmentid; }
	public Integer getMindurationYears() { return mindurationYears; }

	public void setMindurationYears(Integer mindurationYears) { this.mindurationYears = mindurationYears; }

	public Integer getMaxdurationYears() { return maxdurationYears; }

	public void setMaxdurationYears(Integer maxdurationYears) { this.maxdurationYears = maxdurationYears; }

	public List<Scholars> getScholars() { return scholars; }

	public void setScholars(List<Scholars> scholars) { this.scholars = scholars; }

  
}
   

