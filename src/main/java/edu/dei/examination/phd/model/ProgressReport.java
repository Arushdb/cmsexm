package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_reports")
public class ProgressReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholar_id", nullable = false)
    private Scholars scholar;

    private Integer semesterId;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String committeeState;
    private LocalDate meetingDate;
    @Column(columnDefinition = "TEXT")
    private String nextActions;

    private LocalDateTime createdAt;

    public ProgressReport() {}

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    // getters & setters
    public Integer getReportId() { return reportId; }
    public void setReportId(Integer reportId) { this.reportId = reportId; }
    public Scholars getScholar() { return scholar; }
    public void setScholar(Scholars scholar) { this.scholar = scholar; }
    public Integer getSemesterId() { return semesterId; }
    public void setSemesterId(Integer semesterId) { this.semesterId = semesterId; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCommitteeState() { return committeeState; }
    public void setCommitteeState(String committeeState) { this.committeeState = committeeState; }
    public LocalDate getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDate meetingDate) { this.meetingDate = meetingDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public String getNextActions() {
		return nextActions;
	}

	public void setNextActions(String nextActions) {
		this.nextActions = nextActions;
	}
    
}
