package edu.dei.examination.cmsexm.model;
import javax.persistence.*;




import java.util.Date;

	
	@Entity
	@Table(name = "dg_controller", schema = "exam_live")
	public class Dgmain {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(name = "program_course_key", nullable = false, length = 50)
	    private String programCourseKey;

	    @Column(name = "roll_number", length = 15)
	    private String rollNumber;

	    @Column(name = "semester_start_date")
	    @Temporal(TemporalType.DATE)
	    private Date semesterStartDate;

	    @Column(name = "semester_end_date")
	    @Temporal(TemporalType.DATE)
	    private Date semesterEndDate;

	    @Column(nullable = false, length = 2)
	    private String status ;

	    @Column(nullable = false, length = 2)
	    private String extract;

	    @Column(name = "extracted_count")
	    private Integer extractedCount;

	    @Column(name = "email_send")
	    private boolean emailSend ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "email_send_date")
	    private Date emailSendDate;

	    @Column(nullable = false)
	    private boolean uploaded ;

	    @Column(name = "upload_by", length = 100)
	    private String uploadBy;

	    @Column(name = "upload_time")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date uploadTime;

	    @Column(name = "extract_by", length = 100)
	    private String extractBy;

	  
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "insert_time")
	    private Date insertTime;

	   

	    // Getters and Setters

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getProgramCourseKey() {
	        return programCourseKey;
	    }

	    public void setProgramCourseKey(String programCourseKey) {
	        this.programCourseKey = programCourseKey;
	    }

	    public String getRollNumber() {
	        return rollNumber;
	    }

	    public void setRollNumber(String rollNumber) {
	        this.rollNumber = rollNumber;
	    }

	    public Date getSemesterStartDate() {
	        return semesterStartDate;
	    }

	    public void setSemesterStartDate(Date semesterStartDate) {
	        this.semesterStartDate = semesterStartDate;
	    }

	    public Date getSemesterEndDate() {
	        return semesterEndDate;
	    }

	    public void setSemesterEndDate(Date semesterEndDate) {
	        this.semesterEndDate = semesterEndDate;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public String getExtract() {
	        return extract;
	    }

	    public void setExtract(String extract) {
	        this.extract = extract;
	    }

	    public Integer getExtractedCount() {
	        return extractedCount;
	    }

	    public void setExtractedCount(Integer extractedCount) {
	        this.extractedCount = extractedCount;
	    }

	    public boolean isEmailSend() {
	        return emailSend;
	    }

	    public void setEmailSend(boolean emailSend) {
	        this.emailSend = emailSend;
	    }

	  

	    

	    public boolean isUploaded() {
	        return uploaded;
	    }

	    public void setUploaded(boolean uploaded) {
	        this.uploaded = uploaded;
	    }

	    public String getUploadBy() {
	        return uploadBy;
	    }

	    public void setUploadBy(String uploadBy) {
	        this.uploadBy = uploadBy;
	    }

	  

	    public String getExtractBy() {
	        return extractBy;
	    }

	    public void setExtractBy(String extractBy) {
	        this.extractBy = extractBy;
	    }

		public Date getEmailSendDate() {
			return emailSendDate;
		}

		public void setEmailSendDate(Date emailSendDate) {
			this.emailSendDate = emailSendDate;
		}

		public Date getUploadTime() {
			return uploadTime;
		}

		public void setUploadTime(Date uploadTime) {
			this.uploadTime = uploadTime;
		}

		public Date getInsertTime() {
			return insertTime;
		}

		public void setInsertTime(Date insertTime) {
			this.insertTime = insertTime;
		}

		public Dgmain() {
			
		}

	   

	   
	}



