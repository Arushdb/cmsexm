package edu.dei.examination.phd.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "progress_work")
public class ProgressWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_id", nullable = false)
    private Integer reportId;

    private String stage;

    @Column(name = "objective_no")
    private String objectiveNo;

    @Column(name = "completion_percentage")
    private Integer completionPercentage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getReportId() { return reportId; }

	public void setReportId(Integer reportId) { this.reportId = reportId; }

	public String getStage() { return stage; }

	public void setStage(String stage) { this.stage = stage; }

	public String getObjectiveNo() { return objectiveNo; }

	public void setObjectiveNo(String objectiveNo) { this.objectiveNo = objectiveNo; }

	public Integer getCompletionPercentage() { return completionPercentage; }

	public void setCompletionPercentage(Integer completionPercentage) { this.completionPercentage = completionPercentage; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public String getCreatedBy() { return createdBy; }

	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    
    // Getters and setters
    
}