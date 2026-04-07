package edu.dei.examination.phd.model;


import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
    name = "scholar_semesters",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"scholar_id", "semester_id"}
    )
)
public class ScholarSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "scholar_id", nullable = false)
    private Integer scholarId;

//    @Column(name = "semester_id", nullable = false,insertable = false,updatable = false)
//    private Integer semesterId;

      @Column(name = "overall_remarks")
    private String overallRemarks;

    
    
  
    
 // 🧾 Audit
    @CreationTimestamp
    @Column(name = "created_at",columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATETIME") 
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    
    @ManyToOne
    @JoinColumn(name = "scholar_id", insertable = false, updatable = false)
    private Scholars scholar;
    
 // ⚡ Optimization fields
    @Column(name = "program_id")
    private Integer programId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "faculty_id")
    private Integer facultyId;
    
    // 📅 Academic
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    
    // ✅ Attendance
    @Column(name = "total_sessions")
    private Integer totalsessions;

    @Column(name = "attended_sessions")
    private Integer attendedsessions;

    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private Double attendancePercentage;

    @Column(name = "attendance_remarks")
    private String attendanceremarks;
    
    // 📊 Review
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus = ReviewStatus.Pending;
    
    @JsonIgnore
    @OneToMany(mappedBy = "scholarSemester")
    private List<ProgressReport> progressReports;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semesters semester;

    @JsonIgnore
    @OneToMany(mappedBy = "scholarSemester")
    private List<Report> reports;
   

    /* ---------- Enum ---------- */
    public enum ReviewStatus {
        Pending,
        Approved,
        Rejected
    }

    /* ---------- Getters & Setters ---------- */

 

    public Integer getScholarId() {
        return scholarId;
    }

    public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public void setScholarId(Integer scholarId) {
        this.scholarId = scholarId;
    }

//    public Integer getSemesterId() {
//        return semesterId;
//    }
//
//    public void setSemesterId(Integer semesterId) {
//        this.semesterId = semesterId;
//    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getOverallRemarks() {
        return overallRemarks;
    }

    public void setOverallRemarks(String overallRemarks) {
        this.overallRemarks = overallRemarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

//    @PrePersist
//    public void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }

	

	public String getCreatedBy() { return createdBy; }

	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public Integer getTotalsessions() { return totalsessions; }

	public void setTotalsessions(Integer totalsessions) { this.totalsessions = totalsessions; }

	public Integer getAttendedsessions() { return attendedsessions; }

	public void setAttendedsessions(Integer attendedsessions) { this.attendedsessions = attendedsessions; }

	public String getAttendanceremarks() { return attendanceremarks; }

	public void setAttendanceremarks(String attendanceremarks) { this.attendanceremarks = attendanceremarks; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public String getUpdatedBy() { return updatedBy; }

	public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

	public Scholars getScholar() { return scholar; }

	public void setScholar(Scholars scholar) { this.scholar = scholar; }

	public Integer getProgramId() { return programId; }

	public void setProgramId(Integer programId) { this.programId = programId; }

	public Integer getDepartmentId() { return departmentId; }

	public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

	public Integer getFacultyId() { return facultyId; }

	public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }

	public Semesters getSemester() { return semester; }

	public void setSemester(Semesters semester) { this.semester = semester; }

	public List<Report> getReports() { return reports; }

	public void setReports(List<Report> reports) { this.reports = reports; }

	public List<ProgressReport> getProgressReports() { return progressReports; }

	public void setProgressReports(List<ProgressReport> progressReports) { this.progressReports = progressReports; }
	
	
	

	
    
    
    
}
