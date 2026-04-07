package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_history")
public class ReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔗 Link to Report
    @Column(name = "report_id", nullable = false)
    private Integer reportId;

    // 🔗 Role (Supervisor, Reviewer, HOD, Dean)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    // 🔗 User who performed action
    @Column(name = "acted_by", nullable = false)
    private Integer actedBy;

    // 🔄 Action taken
    @Column(name = "action", nullable = false)
    private String action; // APPROVED / REJECTED

    // 📝 Remarks
    @Column(name = "remarks")
    private String remarks;

    // ⏱ Timestamp
    @Column(name = "acted_at")
    private LocalDateTime actedAt;

    /* ---------- Lifecycle ---------- */

    @PrePersist
    public void onCreate() {
        this.actedAt = LocalDateTime.now();
    }

    /* ---------- Getters & Setters ---------- */

    public Integer getId() { return id; }

    public Integer getReportId() { return reportId; }
    public void setReportId(Integer reportId) { this.reportId = reportId; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getActedBy() { return actedBy; }
    public void setActedBy(Integer actedBy) { this.actedBy = actedBy; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getActedAt() { return actedAt; }
    public void setActedAt(LocalDateTime actedAt) { this.actedAt = actedAt; }
}