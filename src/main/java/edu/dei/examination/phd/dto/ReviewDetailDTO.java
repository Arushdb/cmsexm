package edu.dei.examination.phd.dto;

import java.util.List;

import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ReviewerRemark;

public class ReviewDetailDTO {

    private ProgressReport report;
    private Double attendancePercentage;
    private String previousRemarks;
    private String fullName;
    private String enrolmentno;
    private String programname;
   // private List<ReviewerRemark> thereviewerRemark;

    public ReviewDetailDTO(
            ProgressReport report,
            Double attendancePercentage,
            String previousRemarks,
            String fullName,
            String enrolmentno,
            String programname
            //List<ReviewerRemark> thereviewerRemark
            ) {

        this.report = report;
        this.attendancePercentage = attendancePercentage;
        this.previousRemarks = previousRemarks;
        this.fullName=fullName;
        this.enrolmentno=enrolmentno;
        this.programname=programname;
        //this.thereviewerRemark=thereviewerRemark;
    }

	public ProgressReport getReport() { return report; }

	public void setReport(ProgressReport report) { this.report = report; }

	public Double getAttendancePercentage() { return attendancePercentage; }

	public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

	public String getPreviousRemarks() { return previousRemarks; }

	public void setPreviousRemarks(String previousRemarks) { this.previousRemarks = previousRemarks; }

	public String getFullName() { return fullName; }

	public void setFullName(String fullName) { this.fullName = fullName; }

	public String getEnrolmentno() { return enrolmentno; }

	public void setEnrolmentno(String enrolmentno) { this.enrolmentno = enrolmentno; }

	public String getProgramname() { return programname; }

	public void setProgramname(String programname) { this.programname = programname; }

	//public List<ReviewerRemark> getThereviewerRemark() { return thereviewerRemark; }

	//public void setThereviewerRemark(List<ReviewerRemark> thereviewerRemark) { this.thereviewerRemark = thereviewerRemark; }

	
	

    // getters
    
    
}