
package edu.dei.examination.cmsexm.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.RefreshToken;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    // Create and save refresh token during login
    @Transactional
    public RefreshToken createRefreshToken(
            User user, String applicationCode) {

        RefreshToken token = new RefreshToken();

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setApplicationCode(applicationCode);
        token.setExpiryDate(
                Instant.now().plus(7, ChronoUnit.DAYS));

        token.setRevoked(false);

        return repository.save(token);
    }

    // Verify refresh token
    public RefreshToken verify(
            String tokenValue, String applicationCode) {

        RefreshToken token = repository.findByToken(tokenValue)
                .orElseThrow(() ->
                    new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        if (!applicationCode.equalsIgnoreCase(
                token.getApplicationCode())) {

            throw new RuntimeException(
                    "Refresh token application mismatch");
        }

        return token;
    }

    // Rotate refresh token
    @Transactional
    public RefreshToken rotate(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        repository.save(oldToken);

        RefreshToken newToken = new RefreshToken();

        newToken.setUser(oldToken.getUser());

        newToken.setToken(UUID.randomUUID().toString());

        // Preserve application identity
        newToken.setApplicationCode(
                oldToken.getApplicationCode());

        newToken.setExpiryDate(
                Instant.now().plus(7, ChronoUnit.DAYS));

        newToken.setRevoked(false);

        return repository.save(newToken);
    }
}
