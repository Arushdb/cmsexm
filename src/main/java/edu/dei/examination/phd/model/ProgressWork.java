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
    @Column(name = "id")
    private Integer progressWorkId;

    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "stage")
    private String stageOfResearch;

    @Column(name = "objective_no")
    private String objectiveNo;

    @Column(name = "completion_percentage")
    private Double completionPercentage;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public Integer getProgressWorkId() { return progressWorkId; }

	public void setProgressWorkId(Integer progressWorkId) { this.progressWorkId = progressWorkId; }

	public Integer getReportId() { return reportId; }

	public void setReportId(Integer reportId) { this.reportId = reportId; }

	public String getStageOfResearch() { return stageOfResearch; }

	public void setStageOfResearch(String stageOfResearch) { this.stageOfResearch = stageOfResearch; }

	public String getObjectiveNo() { return objectiveNo; }

	public void setObjectiveNo(String objectiveNo) { this.objectiveNo = objectiveNo; }

	public Double getCompletionPercentage() { return completionPercentage; }

	public void setCompletionPercentage(Double completionPercentage) { this.completionPercentage = completionPercentage; }

	

	public LocalDateTime getCreatedAt() { return createdAt; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public String getCreatedBy() { return createdBy; }

	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
	
	

    // getters setters
    
    
}