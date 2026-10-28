package edu.dei.examination.phd.model;


import javax.persistence.*;

@Entity
@Table(
    name = "subject_program_mapping",
    schema = "phd"
)
public class SubjectProgramMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", nullable = false, length = 200)
    private String subjectName;

    @Column(
        name = "subject_key",
        nullable = false,
        unique = true,
        length = 200
    )
    private String subjectKey;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "program_id", nullable = false)
    private Long programId;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    // Default constructor required by JPA
    public SubjectProgramMapping() {
    }

    public SubjectProgramMapping(
            String subjectName,
            String subjectKey,
            Long departmentId,
            Long programId) {

        this.subjectName = subjectName;
        this.subjectKey = subjectKey;
        this.departmentId = departmentId;
        this.programId = programId;
        this.active = Boolean.TRUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}