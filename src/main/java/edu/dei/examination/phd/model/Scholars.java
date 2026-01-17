package edu.dei.examination.phd.model;




import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "scholars",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "registration_no"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "application_number"),
        @UniqueConstraint(columnNames = "enrolmentno")
    }
)
public class Scholars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scholar_id")
    private Integer scholarId;

    @Column(name = "program_id", nullable = false)
    private Integer programId;

    @Column(name = "registration_no")
    private String registrationNo;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender_id")
    private Integer genderId;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "category")
    private String category;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Column(name = "expected_completion")
    private LocalDate expectedCompletion;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "primary_supervisor_id")
    private Integer primarySupervisorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "application_number")
    private String applicationNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "enrolmentno")
    private Integer enrolmentno;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "fathername")
    private String fathername;

    @Column(name = "mothername")
    private String mothername;

    @Column(name = "addressforcorespondence")
    private String addressForCorrespondence;

    @Lob
    @Column(name = "nameinhindi")
    private byte[] nameInHindi;

    @Column(name = "registrationdate")
    private LocalDate registrationDate;

    @Column(name = "dateJRF")
    private LocalDate dateJrf;

    @Column(name = "dateJRFexp")
    private LocalDate dateJrfExp;

    @Column(name = "dateextension")
    private LocalDate dateExtension;

    @Column(name = "researchtopiceng")
    private String researchTopicEng;

    @Column(name = "researchtopichnd")
    private String researchTopicHnd;

    @Column(name = "subject")
    private String subject;

    @Column(name = "supervisor_id")
    private Integer supervisorId;

    @Column(name = "co_supervisor_id")
    private Integer coSupervisorId;

    @Column(name = "admission_session")
    private String admissionSession;

    @Column(name = "mode")
    private String mode;

    /* ---------- AUTO TIMESTAMP ---------- */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /* ---------- GETTERS & SETTERS ---------- */

    public Integer getScholarId() {
        return scholarId;
    }

    public void setScholarId(Integer scholarId) {
        this.scholarId = scholarId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEnrolmentno() {
        return enrolmentno;
    }

    public void setEnrolmentno(Integer enrolmentno) {
        this.enrolmentno = enrolmentno;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
}

///////////////////////////////////////////////
    

