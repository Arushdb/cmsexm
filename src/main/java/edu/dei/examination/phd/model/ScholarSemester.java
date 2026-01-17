package edu.dei.examination.phd.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Column(name = "scholar_semester_id")
    private Integer scholarSemesterId;

    @Column(name = "scholar_id", nullable = false)
    private Integer scholarId;

    @Column(name = "semester_id", nullable = false)
    private Integer semesterId;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private Double attendancePercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus = ReviewStatus.Pending;

    @Column(name = "overall_remarks")
    private String overallRemarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /* ---------- Enum ---------- */
    public enum ReviewStatus {
        Pending,
        Approved,
        Rejected
    }

    /* ---------- Getters & Setters ---------- */

    public Integer getScholarSemesterId() {
        return scholarSemesterId;
    }

    public void setScholarSemesterId(Integer scholarSemesterId) {
        this.scholarSemesterId = scholarSemesterId;
    }

    public Integer getScholarId() {
        return scholarId;
    }

    public void setScholarId(Integer scholarId) {
        this.scholarId = scholarId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

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

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
