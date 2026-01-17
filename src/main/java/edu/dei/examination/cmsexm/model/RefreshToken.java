package edu.dei.examination.cmsexm.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false,name =  "expiry_date")
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getToken() { return token; }

	public void setToken(String token) { this.token = token; }

	public User getUser() { return user; }

	public void setUser(User user) { this.user = user; }

	public Instant getExpiryDate() { return expiryDate; }

	public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

	public boolean isRevoked() { return revoked; }

	public void setRevoked(boolean revoked) { this.revoked = revoked; }

    // getters/setters
    
}

