package edu.dei.examination.phd.model;

import javax.persistence.*;

@Entity
@Table(name = "achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "awards", columnDefinition = "TEXT")
    private String awards;

    @Column(name = "patents", columnDefinition = "TEXT")
    private String patents;

    @Column(name = "teaching_hours")
    private Integer teachingHours;

    @Column(name = "teaching_type", length = 50)
    private String teachingType;

    public Achievement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPatents() {
        return patents;
    }

    public void setPatents(String patents) {
        this.patents = patents;
    }

    public Integer getTeachingHours() {
        return teachingHours;
    }

    public void setTeachingHours(Integer teachingHours) {
        this.teachingHours = teachingHours;
    }

    public String getTeachingType() {
        return teachingType;
    }

    public void setTeachingType(String teachingType) {
        this.teachingType = teachingType;
    }
}