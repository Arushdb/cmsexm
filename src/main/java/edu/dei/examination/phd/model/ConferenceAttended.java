package edu.dei.examination.phd.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "conference")
public class ConferenceAttended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_id", nullable = false)
    private Integer reportId;
    
    @Column(columnDefinition = "TEXT")
    private String authors;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(length = 100)
    private String type;

    @Column(length = 50)
    private String level;

    @Column(length = 255)
    private String organizer;

    @Column(length = 100)
    private String place;

    @Column(length = 100)
    private String dates;

    @Column(name = "presentation_type", length = 50)
    private String presentationType;

    @Column(length = 50)
    private String participation;

    @Column(columnDefinition = "TEXT")
    private String funding;

	public Integer getReportId() { return reportId; }

	public void setReportId(Integer reportId) { this.reportId = reportId; }

	public String getAuthors() { return authors; }

	public void setAuthors(String authors) { this.authors = authors; }

	public String getTitle() { return title; }

	public void setTitle(String title) { this.title = title; }

	public String getType() { return type; }

	public void setType(String type) { this.type = type; }

	public String getLevel() { return level; }

	public void setLevel(String level) { this.level = level; }

	public String getOrganizer() { return organizer; }

	public void setOrganizer(String organizer) { this.organizer = organizer; }

	public String getPlace() { return place; }

	public void setPlace(String place) { this.place = place; }

	public String getDates() { return dates; }

	public void setDates(String dates) { this.dates = dates; }

	public String getPresentationType() { return presentationType; }

	public void setPresentationType(String presentationType) { this.presentationType = presentationType; }

	public String getParticipation() { return participation; }

	public void setParticipation(String participation) { this.participation = participation; }

	public String getFunding() { return funding; }

	public void setFunding(String funding) { this.funding = funding; }

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }
	
    
    

    // getters and setters
}