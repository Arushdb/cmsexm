package edu.dei.examination.phd.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProgressReportRequest {

    @NotNull(message = "scholarId is required")
    private Integer scholarId;

    private Integer semesterId;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    @NotBlank(message = "summary is required")
    private String summary;

    private String committeeState;
    private LocalDate meetingDate;
    private String nextActions;

    // getters & setters
    public Integer getScholarId() { return scholarId; }
    public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }
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
    public String getNextActions() { return nextActions; }
    public void setNextActions(String nextActions) { this.nextActions = nextActions; }
}
