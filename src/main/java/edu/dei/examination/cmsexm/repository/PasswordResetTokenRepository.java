package edu.dei.examination.cmsexm.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
