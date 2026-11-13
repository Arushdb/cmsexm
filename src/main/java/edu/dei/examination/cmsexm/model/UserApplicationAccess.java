package edu.dei.examination.cmsexm.model;


import javax.persistence.*;

@Entity
@Table(
    name = "user_application_access",
    schema = "exam_live",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "application_code"}
        )
    }
)
public class UserApplicationAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "application_code", nullable = false,
            length = 20)
    private String applicationCode;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }
}
