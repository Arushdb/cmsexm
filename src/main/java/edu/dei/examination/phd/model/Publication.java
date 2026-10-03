package edu.dei.examination.phd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String authors;

    @Column(columnDefinition = "TEXT")
    private String title;

    private String journal;

    @Column(name = "volume")
    private String volume;

    @Column(name = "page_no")
    private String pageNo;

    private Integer year;

    @Column(name = "impact_factor")
    private String impact;

    private String indexing;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "report_id", nullable = false)
//    @JsonIgnore
//    private ProgressReport progressReport;
    
    @Column(name = "report_id")
    private Integer reportId;

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getAuthors() { return authors; }

	public void setAuthors(String authors) { this.authors = authors; }

	public String getTitle() { return title; }

	public void setTitle(String title) { this.title = title; }

	public String getJournal() { return journal; }

	public void setJournal(String journal) { this.journal = journal; }

	public String getVolume() { return volume; }

	public void setVolume(String volume) { this.volume = volume; }

	public String getPageNo() { return pageNo; }

	public void setPageNo(String pageNo) { this.pageNo = pageNo; }

	public Integer getYear() { return year; }

	public void setYear(Integer year) { this.year = year; }

	
	public String getIndexing() { return indexing; }

	public void setIndexing(String indexing) { this.indexing = indexing; }

//	public ProgressReport getProgressReport() { return progressReport; }
//
//	public void setProgressReport(ProgressReport progressReport) { this.progressReport = progressReport; }

	public String getImpact() { return impact; }

	public void setImpact(String impact) { this.impact = impact; }

	public Integer getReportId() { return reportId; }

	public void setReportId(Integer reportId) { this.reportId = reportId; }

	
	
    // Getters and Setters
    
}