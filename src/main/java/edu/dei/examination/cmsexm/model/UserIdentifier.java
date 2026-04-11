package edu.dei.examination.cmsexm.model;


import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
    name = "exam_live.user_identifiers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"identifier_type", "identifier_value"})
    },
    schema = "exam_live"
)
public class UserIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /* =========================
       USER MAPPING
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /* =========================
       IDENTIFIER DETAILS
       ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type", nullable = false, length = 30)
    private IdentifierType identifierType;

    @Column(name = "identifier_value", nullable = false, length = 50)
    private String identifierValue;
    
    public enum IdentifierType {
        APPLICATION_NO,
        REGISTRATION_NO,
        ENROLLMENT_NO
    }

    /* =========================
       STATUS
       ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private Status status = Status.ACTIVE;
    
    public enum Status {
        ACTIVE,
        INACTIVE
    }
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* =========================
       CONSTRUCTORS
       ========================= */
    public UserIdentifier() {
    }

    public UserIdentifier(User user,
                          IdentifierType identifierType,
                          String identifierValue) {
        this.user = user;
        this.identifierType = identifierType;
        this.identifierValue = identifierValue;
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

   


	public Status getStatus() { return status; }

	public void setStatus(Status status) { this.status = status; }

	public LocalDateTime getCreatedAt() { return createdAt; }

	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    
}
