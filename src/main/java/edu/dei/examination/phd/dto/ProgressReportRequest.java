package edu.dei.examination.phd.dto;
import java.time.LocalDate;

public class ProgressReportRequest {

	private Integer semesterRegistrationId;
    private Integer lastSemesterRegistrationId;
    private Double attendence;
    private String researchWork;
    private String conference;
    private String researchPaper;
    private String tours;
    
    private String summary;
    private String nextActions;
	public Integer getSemesterRegistrationId() { return semesterRegistrationId; }
	public void setSemesterRegistrationId(Integer semesterRegistrationId) {
		this.semesterRegistrationId = semesterRegistrationId;
	}
	public Integer getLastSemesterRegistrationId() { return lastSemesterRegistrationId; }
	public void setLastSemesterRegistrationId(Integer lastSemesterRegistrationId) {
		this.lastSemesterRegistrationId = lastSemesterRegistrationId;
	}
	
	
	
	public Double getAttendence() { return attendence; }
	public void setAttendence(Double attendence) { this.attendence = attendence; }
	public String getResearchWork() { return researchWork; }
	public void setResearchWork(String researchWork) { this.researchWork = researchWork; }
	public String getConference() { return conference; }
	public void setConference(String conference) { this.conference = conference; }
	public String getResearchPaper() { return researchPaper; }
	public void setResearchPaper(String researchPaper) { this.researchPaper = researchPaper; }
	public String getTours() { return tours; }
	public void setTours(String tours) { this.tours = tours; }
	
	public String getSummary() { return summary; }
	public void setSummary(String summary) { this.summary = summary; }
	public String getNextActions() { return nextActions; }
	public void setNextActions(String nextActions) { this.nextActions = nextActions; }
    
    
    
    
}
