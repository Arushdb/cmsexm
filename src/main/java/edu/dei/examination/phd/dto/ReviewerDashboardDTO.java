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
    private Integer scholarSemesterId;
    private String status;
    private String submittedOn;
    private String session;
    private String semesterName;
    private Integer totalsessions;
    private Integer attendedsessions;
    private Double attendancePercentage;
    private String attendanceremarks;
    

    // Constructor (used in JPQL query)
    public ReviewerDashboardDTO(
    		Integer scholarId,
    		String enrolmentno,
    		String academicyear,
    		Integer scholarSemesterId,
    		String semesterName,
            String scholarName,
            String programName,
            Integer reportId,
            ProgressStatus status,
            LocalDateTime submittedAt,
            Integer totalsessions,
            Integer attendedsessions,
            Double attendancePercentage,
            String attendanceremarks
            ) {

        this.scholarId = scholarId;
        this.scholarName = scholarName;
        this.programName = programName;
        this.reportId = reportId;
     // Convert enum to string for UI
        this.status = status != null ? status.name() : null;
        this.enrolmentno=enrolmentno;
        this.session=academicyear;
        this.scholarSemesterId=scholarSemesterId;
        this.semesterName=semesterName;
        

        // Convert timestamp to string
        if (submittedAt != null) {
            this.submittedOn = submittedAt.toString();
        }
        this.totalsessions=totalsessions;
        this.attendedsessions=attendedsessions;
        this.attendancePercentage=attendancePercentage;
        this.attendanceremarks=attendanceremarks;
        
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

	public Integer getScholarSemesterId() { return scholarSemesterId; }

	public void setScholarSemesterId(Integer scholarSemesterId) { this.scholarSemesterId = scholarSemesterId; }

	public String getSemesterName() { return semesterName; }

	public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

	public Integer getTotalsessions() { return totalsessions; }

	public void setTotalsessions(Integer totalsessions) { this.totalsessions = totalsessions; }

	public Integer getAttendedsessions() { return attendedsessions; }

	public void setAttendedsessions(Integer attendedsessions) { this.attendedsessions = attendedsessions; }

	public Double getAttendancePercentage() { return attendancePercentage; }

	public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

	public String getAttendanceremarks() { return attendanceremarks; }

	public void setAttendanceremarks(String attendanceremarks) { this.attendanceremarks = attendanceremarks; }
	
	
	
    
    
}