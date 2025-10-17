package edu.dei.examination.cmsexm.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "modular_sem", schema = "exam_live")
public class DgModularSem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String module;

    @Column(name = "program_course_key")
    private String programCourseKey;

    @Column(name = "module_group")
    private String moduleGroup;

    @Column(name = "program_id", nullable = false, length = 45)
    private String programId;
    
    @Column(name = "entity_id", nullable = false, length = 45)
    private String entityId;

    @Column(name = "session_start_date")
    @Temporal(TemporalType.DATE)
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    @Temporal(TemporalType.DATE)
    private Date sessionEndDate;

    private String status;

    // --- Getters & Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProgramCourseKey() {
        return programCourseKey;
    }

    public void setProgramCourseKey(String programCourseKey) {
        this.programCourseKey = programCourseKey;
    }

    public String getModuleGroup() {
        return moduleGroup;
    }

    public void setModuleGroup(String moduleGroup) {
        this.moduleGroup = moduleGroup;
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
}
