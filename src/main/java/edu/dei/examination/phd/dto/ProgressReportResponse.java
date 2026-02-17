package edu.dei.examination.phd.dto;

import edu.dei.examination.phd.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProgressReportResponse {

    private Integer id;
    private Integer semesterRegistrationId;
    private Integer lastSemesterRegistrationId;

    private Double attendence;

    private String researchWork;
    private String conference;
    private String researchPaper;
    private String tours;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private String summary;
    private String nextActions;

    private ProgressStatus progressStatus;

    private Boolean committeeState;
    private LocalDate meetingDate;

    private LocalDateTime insertTime;

    public ProgressReportResponse() {
    }

    /* ================= GETTERS & SETTERS ================= */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSemesterRegistrationId() {
        return semesterRegistrationId;
    }

    public void setSemesterRegistrationId(Integer semesterRegistrationId) {
        this.semesterRegistrationId = semesterRegistrationId;
    }

    public Integer getLastSemesterRegistrationId() {
        return lastSemesterRegistrationId;
    }

    public void setLastSemesterRegistrationId(Integer lastSemesterRegistrationId) {
        this.lastSemesterRegistrationId = lastSemesterRegistrationId;
    }

    public Double getAttendence() {
        return attendence;
    }

    public void setAttendence(Double attendence) {
        this.attendence = attendence;
    }

    public String getResearchWork() {
        return researchWork;
    }

    public void setResearchWork(String researchWork) {
        this.researchWork = researchWork;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getResearchPaper() {
        return researchPaper;
    }

    public void setResearchPaper(String researchPaper) {
        this.researchPaper = researchPaper;
    }

    public String getTours() {
        return tours;
    }

    public void setTours(String tours) {
        this.tours = tours;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNextActions() {
        return nextActions;
    }

    public void setNextActions(String nextActions) {
        this.nextActions = nextActions;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public Boolean getCommitteeState() {
        return committeeState;
    }

    public void setCommitteeState(Boolean committeeState) {
        this.committeeState = committeeState;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(LocalDateTime insertTime) {
        this.insertTime = insertTime;
    }
}
