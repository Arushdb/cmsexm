package edu.dei.examination.cmsexm.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pck_change_controller")
public class PckChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_pck")
    private String oldPck;

    @Column(name = "new_pck")
    private String newPck;

    @Column(name = "semester_start_date")
    @Temporal(TemporalType.DATE)
    private Date semesterStartDate;

    @Column(name = "semester_end_date")
    @Temporal(TemporalType.DATE)
    private Date semesterEndDate;

    @Column(name = "program_id")
    private String programId;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "specialization_id")
    private String specializationId;

    @Column(name = "status")
    private String status;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOldPck() {
        return oldPck;
    }
    public void setOldPck(String oldPck) {
        this.oldPck = oldPck;
    }
    public String getNewPck() {
        return newPck;
    }
    public void setNewPck(String newPck) {
        this.newPck = newPck;
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
    public String getProgramId() {
        return programId;
    }
    public void setProgramId(String programId) {
        this.programId = programId;
    }
    public String getBranchId() {
        return branchId;
    }
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    public String getSpecializationId() {
        return specializationId;
    }
    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
