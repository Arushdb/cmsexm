package edu.dei.examination.cmsexm.model;
import javax.persistence.*;




import java.util.Date;

	
	@Entity
	@Table(name = "degree_dg_controller", schema = "exam_live")
	public class DegreeDgMain {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(name = "program_id", nullable = false, length = 50)
	    private String programId;

	    @Column(name = "roll_number", length = 15)
	    private String rollNumber;

	    @Column(name = "session_start_date")
	    @Temporal(TemporalType.DATE)
	    private Date sessionStartDate;

	    @Column(name = "session_end_date")
	    @Temporal(TemporalType.DATE)
	    private Date sessionEndDate;

	    @Column(nullable = false, length = 2)
	    private String status ;

	  

	   

	   

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

	    public String getProgramId() {
	        return programId;
	    }

	    public void setProgramId(String programId) {
	        this.programId = programId;
	    }

	    public String getRollNumber() {
	        return rollNumber;
	    }

	    public void setRollNumber(String rollNumber) {
	        this.rollNumber = rollNumber;
	    }

	    public Date getSessionStartDate() {
	        return sessionStartDate;
	    }

	    public void setSessionStartDate(Date semesterStartDate) {
	        this.sessionStartDate = semesterStartDate;
	    }

	    public Date getSessionEndDate() {
	        return sessionEndDate;
	    }

	    public void setSessionEndDate(Date semesterEndDate) {
	        this.sessionEndDate = semesterEndDate;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	  
	   
	  

	    

	    
	  

	    public String getExtractBy() {
	        return extractBy;
	    }

	    public void setExtractBy(String extractBy) {
	        this.extractBy = extractBy;
	    }

		

		public Date getInsertTime() {
			return insertTime;
		}

		public void setInsertTime(Date insertTime) {
			this.insertTime = insertTime;
		}

		public DegreeDgMain() {
			
		}

	   

	   
	}



