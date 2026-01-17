package edu.dei.examination.phd.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ResearchTopicRequest {

    @NotNull(message = "scholarId is required")
    private Integer scholarId;

    @NotBlank(message = "title is required")
    private String title;

    private String description;
    private String status; // optional: Proposed, Approved, Active, Completed

    // getters & setters
    public Integer getScholarId() { return scholarId; }
    public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
