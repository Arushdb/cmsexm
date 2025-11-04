package edu.dei.examination.cmsexm.model;
import javax.persistence.*;




import java.util.Date;

	
	@Entity
	@Table(name = "dg_modular_controller", schema = "exam_live")
	public class DgModularMain {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(name = "program_id", nullable = false, length = 45)
	    private String programId;
	    
	    @Column(name = "entity_id", nullable = false, length = 45)
	    private String entityId;

	    @Column(name = "module_group", length = 15)
	    private String moduleGroup;

	    @Column(name = "session_start_date")
	    @Temporal(TemporalType.DATE)
	    private Date sessionStartDate;

	    @Column(name = "session_end_date")
	    @Temporal(TemporalType.DATE)
	    private Date sessionEndDate;

	    @Column(nullable = false, length = 2)
	    private String status ;

	   
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
	    
	    public String getEntityId() {
	        return entityId;
	    }

	    public void setEntityId(String entityId) {
	        this.entityId = entityId;
	    }

	    public String getModuleGroup() {
	        return moduleGroup;
	    }

	    public void setModuleGroup(String moduleGroup) {
	        this.moduleGroup = moduleGroup;
	    }

	    public Date getSessionStartDate() {
	        return sessionStartDate;
	    }

	    public void setSessionStartDate(Date sessionStartDate) {
	        this.sessionStartDate = sessionStartDate;
	    }

	    public Date getSessionEndDate() {
	        return sessionEndDate;
	    }

	    public void setSessionEndDate(Date sessionEndDate) {
	        this.sessionEndDate = sessionEndDate;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	   
		public Date getInsertTime() {
			return insertTime;
		}

		public void setInsertTime(Date insertTime) {
			this.insertTime = insertTime;
		}

		public DgModularMain() {
			
		}

	   

	   
	}



