package edu.dei.examination.phd.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.dei.examination.phd.enums.ProgressStatus;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    // 🔗 Link to Scholar Semester
//   @Column(name = "scholar_semester_id", nullable = false,insertable = false,updatable = false)
//    private Integer scholarSemesterId;

    // ⚡ Optimization fields (avoid joins)
    @Column(name = "program_id")
    private Integer programId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "faculty_id")
    private Integer facultyId;

    // 🔄 Workflow tracking
    @Column(name = "current_sequence_no")
    private Integer currentSequenceNo;

//    // 📊 Status tracking
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private ReportStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressStatus status;
    // 📅 Submission
    @Column(name = "submitted_on")
    private LocalDateTime submittedOn;

    // 🧾 Audit
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

 // 🧾 Audit - Update ✅ (Added)
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "scholar_semester_id")
    private ScholarSemester scholarSemester;
    
    @Column(name = "progress_report_id")
    private Integer progressReportId;
    
   @OneToOne
    @JoinColumn(name = "progress_report_id", insertable = false, updatable = false)
    private ProgressReport progressReport;
    
    
   
    
    // ================= ENUM =================

//    public enum ReportStatus {
//        SUBMITTED,
//        IN_REVIEW,
//        APPROVED,
//        REJECTED
//    }

    // ================= GETTERS & SETTERS =================

    public Integer getId() {
        return id;
    }

//    public Integer getScholarSemesterId() {
//        return scholarSemesterId;
//    }
//
//    public void setScholarSemesterId(Integer scholarSemesterId) {
//        this.scholarSemesterId = scholarSemesterId;
//    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getCurrentSequenceNo() {
        return currentSequenceNo;
    }

    public void setCurrentSequenceNo(Integer currentSequenceNo) {
        this.currentSequenceNo = currentSequenceNo;
    }

   

    public ProgressStatus getStatus() { return status; }

	public void setStatus(ProgressStatus status) { this.status = status; }

	public LocalDateTime getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(LocalDateTime submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

	public String getUpdatedBy() { return updatedBy; }

	public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public ScholarSemester getScholarSemester() { return scholarSemester; }

	public void setScholarSemester(ScholarSemester scholarSemester) { this.scholarSemester = scholarSemester; }

	public Integer getProgressReportId() { return progressReportId; }

	public void setProgressReportId(Integer progressReportId) { this.progressReportId = progressReportId; }

	public ProgressReport getProgressReport() { return progressReport; }

	public void setProgressReport(ProgressReport progressReport) { this.progressReport = progressReport; }
    
    
}