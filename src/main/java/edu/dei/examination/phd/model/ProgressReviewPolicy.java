package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_review_policy")
public class ProgressReviewPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Integer policyId;

    // 🔗 Program mapping
    @Column(name = "program_id", nullable = false)
    private Integer programId;

    // 🔗 Role mapping (SUPERVISOR, REVIEWER, HOD, DEAN)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    // 🔄 Workflow order
    @Column(name = "sequence_no", nullable = false)
    private Integer sequenceNo;

    // ⚠️ Mandatory or optional step
    @Column(name = "is_mandatory")
    private Boolean isMandatory;

    // 🧾 Audit
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "created_by")
//    private String createdBy;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "updated_by")
//    private String updatedBy;

    /* ---------- Lifecycle ---------- */

//    @PrePersist
//    public void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }

    /* ---------- Getters & Setters ---------- */

    public Integer getPolicyId() { return policyId; }

    public Integer getProgramId() { return programId; }
    public void setProgramId(Integer programId) { this.programId = programId; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }

  
}