package edu.dei.examination.cmsexm.model;


import javax.persistence.*;

@Entity
@Table(
    name = "user_identifiers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"identifier_type", "identifier_value"})
    }
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
    private User user;

    /* =========================
       IDENTIFIER DETAILS
       ========================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type", nullable = false, length = 30)
    private IdentifierType identifierType;

    @Column(name = "identifier_value", nullable = false, length = 50)
    private String identifierValue;

    /* =========================
       STATUS
       ========================= */
    @Column(name = "status", length = 10)
    private String status = "ACTIVE";

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
