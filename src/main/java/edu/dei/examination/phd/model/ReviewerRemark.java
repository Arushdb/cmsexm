package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviewer_remarks")
public class ReviewerRemark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer remarkId;

    @Column(name = "review_context", length = 64, nullable = false)
    private String reviewContext; // e.g. PROGRESS_REPORT, RESEARCH_TOPIC

    @Column(name = "context_id", nullable = false)
    private Integer contextId; // e.g. reportId or topicId

    @Column(name = "reviewer_role", length = 64)
    private String reviewerRole; // Supervisor, Co-Supervisor, HOD, DEAN

    @Column(name = "reviewer_id")
    private Integer reviewerId; // optional link to supervisors table

    @Column(name = "remark_text", columnDefinition = "TEXT", nullable = false)
    private String remarkText;

    @Column(name = "is_private")
    private Boolean isPrivate = Boolean.FALSE;

    @Column(name = "remark_date")
    private LocalDateTime remarkDate;

    public ReviewerRemark() {}

    @PrePersist
    public void prePersist() { remarkDate = LocalDateTime.now(); }

    // getters & setters
    public Integer getRemarkId() { return remarkId; }
    public void setRemarkId(Integer remarkId) { this.remarkId = remarkId; }
    public String getReviewContext() { return reviewContext; }
    public void setReviewContext(String reviewContext) { this.reviewContext = reviewContext; }
    public Integer getContextId() { return contextId; }
    public void setContextId(Integer contextId) { this.contextId = contextId; }
    public String getReviewerRole() { return reviewerRole; }
    public void setReviewerRole(String reviewerRole) { this.reviewerRole = reviewerRole; }
    public Integer getReviewerId() { return reviewerId; }
    public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
    public String getRemarkText() { return remarkText; }
    public void setRemarkText(String remarkText) { this.remarkText = remarkText; }
    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
    public LocalDateTime getRemarkDate() { return remarkDate; }
    public void setRemarkDate(LocalDateTime remarkDate) { this.remarkDate = remarkDate; }
}

