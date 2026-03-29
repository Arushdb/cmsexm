package edu.dei.examination.phd.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import edu.dei.examination.phd.enums.ProgressStatus;

public class ReviewerDashboardDTO {

    private Integer scholarId;
    private String scholarName;
    private String enrolmentno;
    private String programName;

    private Integer reportId;
    private String status;
    private String submittedOn;
    private String session;
    

    // Constructor (used in JPQL query)
    public ReviewerDashboardDTO(
    		Integer scholarId,
    		String enrolmentno,
    		String academicyear,
            String scholarName,
            String programName,
            Integer reportId,
            ProgressStatus status,
            LocalDateTime submittedAt) {

        this.scholarId = scholarId;
        this.scholarName = scholarName;
        this.programName = programName;
        this.reportId = reportId;
     // Convert enum to string for UI
        this.status = status != null ? status.name() : null;
        this.enrolmentno=enrolmentno;
        this.session=academicyear;
        

        // Convert timestamp to string
        if (submittedAt != null) {
            this.submittedOn = submittedAt.toString();
        }
    }

    // Getters

    public Integer getScholarId() {
        return scholarId;
    }

    public String getScholarName() {
        return scholarName;
    }

    public String getProgramName() {
        return programName;
    }

    public Integer getReportId() {
        return reportId;
    }

    public String getStatus() {
        return status;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

	public String getEnrolmentno() { return enrolmentno; }

	public void setEnrolmentno(String enrolmentno) { this.enrolmentno = enrolmentno; }

	public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }

	public void setScholarName(String scholarName) { this.scholarName = scholarName; }

	public void setProgramName(String programName) { this.programName = programName; }

	public void setReportId(Integer reportId) { this.reportId = reportId; }

	public void setStatus(String status) { this.status = status; }

	public void setSubmittedOn(String submittedOn) { this.submittedOn = submittedOn; }

	public String getSession() { return session; }

	public void setSession(String session) { this.session = session; }
    
    
}