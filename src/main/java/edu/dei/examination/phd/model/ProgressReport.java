package edu.dei.examination.phd.model ;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.dei.examination.phd.enums.ProgressStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_report")
public class ProgressReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

       
    @ManyToOne
    @JoinColumn(name = "scholar_semester_id")
    private ScholarSemester scholarSemester;

    @Column(name = "last_semester_registration_id")
    private Integer lastSemesterRegistrationId;

    

    @Column(name="research_work")
    private String researchWork;
    private String conference;
    @Column(name = "research_paper")
    private String researchPaper;
    private String tours;

    @Column(name = "insert_time")
    private LocalDateTime insertTime;
    
    
    @Enumerated(EnumType.STRING)        
    @Column(name = "progress_status")
    private ProgressStatus  progressStatus;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    private String summary;

    @Column(name = "committee_state")
    private Boolean committeeState;

    @Column(name = "meeting_date")
    private LocalDate meetingDate;

    @Column(name = "next_actions")
    private String nextActions;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    // 🔁 Inverse side
    @JsonIgnore
    @OneToOne(mappedBy = "progressReport")
    private Report report;

    @PrePersist
    protected void onCreate() {
        this.insertTime = LocalDateTime.now();
        if (this.progressStatus == null) {
            this.progressStatus =ProgressStatus.DRAFT ;
        }
    }

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	

	public Integer getLastSemesterRegistrationId() { return lastSemesterRegistrationId; }

	public void setLastSemesterRegistrationId(Integer lastSemesterRegistrationId) {
		this.lastSemesterRegistrationId = lastSemesterRegistrationId;
	}



	public ScholarSemester getScholarSemester() { return scholarSemester; }

	public void setScholarSemester(ScholarSemester scholarSemester) { this.scholarSemester = scholarSemester; }

	public Report getReport() { return report; }

	public void setReport(Report report) { this.report = report; }

	public String getResearchWork() { return researchWork; }

	public void setResearchWork(String researchWork) { this.researchWork = researchWork; }

	public String getConference() { return conference; }

	public void setConference(String conference) { this.conference = conference; }

	public String getResearchPaper() { return researchPaper; }

	public void setResearchPaper(String researchPaper) { this.researchPaper = researchPaper; }

	public String getTours() { return tours; }

	public void setTours(String tours) { this.tours = tours; }

	public LocalDateTime getInsertTime() { return insertTime; }

	public void setInsertTime(LocalDateTime insertTime) { this.insertTime = insertTime; }

	public ProgressStatus getProgressStatus() { return progressStatus; }

	public void setProgressStatus(ProgressStatus progressStatus) { this.progressStatus = progressStatus; }

	public LocalDate getPeriodStart() { return periodStart; }

	public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }

	public LocalDate getPeriodEnd() { return periodEnd; }

	public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }

	public String getSummary() { return summary; }

	public void setSummary(String summary) { this.summary = summary; }

	public Boolean getCommitteeState() { return committeeState; }

	public void setCommitteeState(Boolean committeeState) { this.committeeState = committeeState; }

	public LocalDate getMeetingDate() { return meetingDate; }

	public void setMeetingDate(LocalDate meetingDate) { this.meetingDate = meetingDate; }

	public String getNextActions() { return nextActions; }

	public void setNextActions(String nextActions) { this.nextActions = nextActions; }

	public LocalDateTime getSubmittedAt() { return submittedAt; }

	public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
	

    
    // getters and setters
}
