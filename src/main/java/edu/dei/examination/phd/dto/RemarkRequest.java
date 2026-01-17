package edu.dei.examination.phd.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RemarkRequest {

    @NotBlank(message = "reviewContext is required")
    private String reviewContext; // e.g. PROGRESS_REPORT

    @NotNull(message = "contextId is required")
    private Integer contextId;

    @NotBlank(message = "remarkText is required")
    private String remarkText;

    private String reviewerRole; // optional (OVERRIDE by auth principal mapping)
    private Integer reviewerId;  // optional (from auth)

    private Boolean isPrivate = false;

    // getters & setters
    public String getReviewContext() { return reviewContext; }
    public void setReviewContext(String reviewContext) { this.reviewContext = reviewContext; }
    public Integer getContextId() { return contextId; }
    public void setContextId(Integer contextId) { this.contextId = contextId; }
    public String getRemarkText() { return remarkText; }
    public void setRemarkText(String remarkText) { this.remarkText = remarkText; }
    public String getReviewerRole() { return reviewerRole; }
    public void setReviewerRole(String reviewerRole) { this.reviewerRole = reviewerRole; }
    public Integer getReviewerId() { return reviewerId; }
    public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }
    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
}
