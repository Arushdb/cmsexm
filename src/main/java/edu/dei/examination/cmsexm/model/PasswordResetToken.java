package edu.dei.examination.cmsexm.model;




import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;
   
    
    @Column(name ="expiry_date")
    private LocalDateTime expiryDate;

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getToken() { return token; }

	public void setToken(String token) { this.token = token; }

	public User getUser() { return user; }

	public void setUser(User user) { this.user = user; }

	public LocalDateTime getExpiryDate() { return expiryDate; }

	public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    // getters and setters
    
}
