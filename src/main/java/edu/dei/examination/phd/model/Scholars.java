package edu.dei.examination.phd.model;




import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "program_id", nullable = false,insertable = false,updatable = false)
    private Integer programId;

    @Column(name = "registration_no")
    private String registrationNo;
    
    private String enrolmentno;

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
    
    @OneToMany(mappedBy = "scholar")
    @JsonIgnore
    private List<ScholarSupervisor> supervisors;
    
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;
    
 // 🔥 Relationship to Department
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
   
  
   
  
    
    
  

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

    public String getEnrolmentno() {
        return enrolmentno;
    }

    public void setEnrolmentno(String enrolmentno) {
        this.enrolmentno = enrolmentno;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

	public String getPhone() { return phone; }

	public void setPhone(String phone) { this.phone = phone; }

	public String getCategory() { return category; }

	public void setCategory(String category) { this.category = category; }

	public LocalDate getAdmissionDate() { return admissionDate; }

	public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

	public LocalDate getExpectedCompletion() { return expectedCompletion; }

	public void setExpectedCompletion(LocalDate expectedCompletion) { this.expectedCompletion = expectedCompletion; }

	public Integer getStatusId() { return statusId; }

	public void setStatusId(Integer statusId) { this.statusId = statusId; }

	public Integer getPrimarySupervisorId() { return primarySupervisorId; }

	public void setPrimarySupervisorId(Integer primarySupervisorId) { this.primarySupervisorId = primarySupervisorId; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDate getDateOfBirth() { return dateOfBirth; }

	public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

	public String getDepartmentCode() { return departmentCode; }

	public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

	public String getFathername() { return fathername; }

	public void setFathername(String fathername) { this.fathername = fathername; }

	public String getMothername() { return mothername; }

	public void setMothername(String mothername) { this.mothername = mothername; }

	public String getAddressForCorrespondence() { return addressForCorrespondence; }

	public void setAddressForCorrespondence(String addressForCorrespondence) {
		this.addressForCorrespondence = addressForCorrespondence;
	}

	public byte[] getNameInHindi() { return nameInHindi; }

	public void setNameInHindi(byte[] nameInHindi) { this.nameInHindi = nameInHindi; }

	public LocalDate getRegistrationDate() { return registrationDate; }

	public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

	public LocalDate getDateJrf() { return dateJrf; }

	public void setDateJrf(LocalDate dateJrf) { this.dateJrf = dateJrf; }

	public LocalDate getDateJrfExp() { return dateJrfExp; }

	public void setDateJrfExp(LocalDate dateJrfExp) { this.dateJrfExp = dateJrfExp; }

	public LocalDate getDateExtension() { return dateExtension; }

	public void setDateExtension(LocalDate dateExtension) { this.dateExtension = dateExtension; }

	public String getResearchTopicEng() { return researchTopicEng; }

	public void setResearchTopicEng(String researchTopicEng) { this.researchTopicEng = researchTopicEng; }

	public String getResearchTopicHnd() { return researchTopicHnd; }

	public void setResearchTopicHnd(String researchTopicHnd) { this.researchTopicHnd = researchTopicHnd; }

	public String getSubject() { return subject; }

	public void setSubject(String subject) { this.subject = subject; }

	public Integer getSupervisorId() { return supervisorId; }

	public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }

	public Integer getCoSupervisorId() { return coSupervisorId; }

	public void setCoSupervisorId(Integer coSupervisorId) { this.coSupervisorId = coSupervisorId; }

	public String getAdmissionSession() { return admissionSession; }

	public void setAdmissionSession(String admissionSession) { this.admissionSession = admissionSession; }

	public String getMode() { return mode; }

	public void setMode(String mode) { this.mode = mode; }

	public Program getProgram() { return program; }

	public void setProgram(Program program) { this.program = program; }

	public List<ScholarSupervisor> getSupervisors() { return supervisors; }

	public void setSupervisors(List<ScholarSupervisor> supervisors) { this.supervisors = supervisors; }

	public Department getDepartment() { return department; }

	public void setDepartment(Department department) { this.department = department; }

	

	

	
    
}

///////////////////////////////////////////////
    

