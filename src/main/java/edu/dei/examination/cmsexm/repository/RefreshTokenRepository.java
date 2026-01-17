package edu.dei.examination.cmsexm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}
