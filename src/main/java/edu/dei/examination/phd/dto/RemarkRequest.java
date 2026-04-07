package edu.dei.examination.phd.dto;


import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RemarkRequest {

    @NotBlank(message = "reviewContext is required")
    private String reviewContext; // e.g. PROGRESS_REPORT

    @NotNull(message = "contextId is required")
    private Integer contextId;

    @NotBlank(message = "remarkText is required")
    private String remarkText;

    private String role; // optional (OVERRIDE by auth principal mapping)
//    private Integer userId;  // optional (from auth)

    private Boolean isPrivate = false;
   
    private Integer parentRemarkId;
    private String  createdAt;
    private String  decision;

    // getters & setters
    public String getReviewContext() { return reviewContext; }
    public void setReviewContext(String reviewContext) { this.reviewContext = reviewContext; }
    public Integer getContextId() { return contextId; }
    public void setContextId(Integer contextId) { this.contextId = contextId; }
    public String getRemarkText() { return remarkText; }
    public void setRemarkText(String remarkText) { this.remarkText = remarkText; }
   
    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
	public Integer getParentRemarkId() { return parentRemarkId; }
	public void setParentRemarkId(Integer parentRemarkId) { this.parentRemarkId = parentRemarkId; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	public String getCreatedAt() { return createdAt; }
	public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
	public String getDecision() { return decision; }
	public void setDecision(String decision) { this.decision = decision; }
	
	
	
	
	
    
    
}
