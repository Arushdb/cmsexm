package edu.dei.examination.cmsexm.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.RefreshToken;
import edu.dei.examination.cmsexm.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    public RefreshToken verify(String tokenValue) {

        RefreshToken token = repository.findByToken(tokenValue)
            .orElseThrow(() ->
                new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }

    public RefreshToken rotate(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        repository.save(oldToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(oldToken.getUser());
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(
            Instant.now().plus(7, ChronoUnit.DAYS)
        );

        return repository.save(newToken);
    }
}
