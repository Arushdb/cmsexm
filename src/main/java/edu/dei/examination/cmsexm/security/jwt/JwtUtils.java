package edu.dei.examination.cmsexm.security.jwt;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;



@Component
public class JwtUtils {

	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${cmsexam.app.jwtSecret}")
	private String jwtSecret;

	@Value("${cmsexam.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	@Value("${cmsexam.app.jwtRefreshExpirationMs}")
	private int jwtRefreshExpirationMs;
	
	@Value("${cmsexam.app.jwtRefreshSecret}")
    private String jwtRefreshSecret;
	
	
	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		Map<String, Object> claims = new HashMap<>();
	    claims.put("userId", userPrincipal.getId());
	   // claims.put("scholarId", userPrincipal.getScholarId());
	    claims.put("role", userPrincipal.getAuthorities()
	                                    .iterator()
	                                    .next()
	                                    .getAuthority());

		return Jwts.builder()
				.setClaims(claims)
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Claims getClaims(String authHeader) {

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        throw new RuntimeException("Invalid Authorization header");
	    }

	    String token = authHeader.substring(7).trim(); // removes "Bearer "

	    return Jwts.parser()
	            .setSigningKey(jwtSecret)  // IMPORTANT
	            .parseClaimsJws(token)
	            .getBody();
	}


	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	
	public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(refreshToken);
            return true;

        } catch (JwtException e) {
            return false;
        }
    }
	
	public String generateAccessTokenFromUsername(String username) {

	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(
	                    System.currentTimeMillis() + jwtExpirationMs
	            ))
	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
	            .compact();
	}


	public String generateRefreshToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

}
