package edu.dei.examination.phd.dto;

import java.time.LocalDate;

public class ScholarDashboardDTO {

    private ScholarDTO scholar;

    private Integer reportId;
    private Integer semesterRegistrationId;
    private String progressStatus;
    private String nextActions;
    private String submittedOn;
    private String semestername;
    private LocalDate startdate;
    private LocalDate enddate;
    private LocalDate deadline;
    private boolean submissionAllowed;
    private LocalDate extensionDeadline;
    
    
    
    
    
	public ScholarDTO getScholar() { return scholar; }
	public void setScholar(ScholarDTO scholar) { this.scholar = scholar; }
	public Integer getReportId() { return reportId; }
	public void setReportId(Integer reportId) { this.reportId = reportId; }
	public Integer getSemesterRegistrationId() { return semesterRegistrationId; }
	public void setSemesterRegistrationId(Integer semesterRegistrationId) {
		this.semesterRegistrationId = semesterRegistrationId;
	}
	public String getProgressStatus() { return progressStatus; }
	public void setProgressStatus(String progressStatus) { this.progressStatus = progressStatus; }
	public String getNextActions() { return nextActions; }
	public void setNextActions(String nextActions) { this.nextActions = nextActions; }
	public String getSubmittedOn() { return submittedOn; }
	public void setSubmittedOn(String submittedOn) { this.submittedOn = submittedOn; }
	public String getSemestername() { return semestername; }
	public void setSemestername(String semestername) { this.semestername = semestername; }
	public LocalDate getStartdate() { return startdate; }
	public void setStartdate(LocalDate startdate) { this.startdate = startdate; }
	public LocalDate getEnddate() { return enddate; }
	public void setEnddate(LocalDate enddate) { this.enddate = enddate; }
	public LocalDate getDeadline() { return deadline; }
	public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
	public boolean isSubmissionAllowed() { return submissionAllowed; }
	public void setSubmissionAllowed(boolean submissionAllowed) { this.submissionAllowed = submissionAllowed; }
	public LocalDate getExtensionDeadline() { return extensionDeadline; }
	public void setExtensionDeadline(LocalDate extensionDeadline) { this.extensionDeadline = extensionDeadline; }
	
	
	
	
	
	

    // getters and setters
    
    
    
}

