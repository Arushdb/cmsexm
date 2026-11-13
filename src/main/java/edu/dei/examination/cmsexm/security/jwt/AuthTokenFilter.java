package edu.dei.examination.cmsexm.security.jwt;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.dei.examination.cmsexm.repository.UserApplicationAccessRepository;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.cmsexm.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserApplicationAccessRepository userApplicationAccessRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthTokenFilter.class);

    private static final String PHD = "PHD";
    private static final String CMSEXAM = "CMSEXAM";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
        	
        	String requestURI = request.getRequestURI();
        	String contextPath = request.getContextPath();

        	// Remove context path
        	String path = requestURI.substring(contextPath.length());

        	// Allow authentication endpoints without access token
        	if (path.startsWith("/cmsexam/api/auth/")
        	        || path.startsWith("/api/auth/")
        	        || "OPTIONS".equalsIgnoreCase(request.getMethod())) {

        	    filterChain.doFilter(request, response);
        	    return;
        	}

            String jwt = parseJwt(request);

            // No token: let Spring Security handle authentication
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 1. Validate JWT signature and expiry
            if (!jwtUtils.validateJwtToken(jwt)) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired access token");
                return;
            }

            // 2. Read signed JWT claims
            Claims claims = jwtUtils.getClaims(
                    request.getHeader("Authorization"));

            String tokenApp = claims.get("app", String.class);

            String requestApp =
                    request.getHeader("X-APPLICATION-ID");

            if (!StringUtils.hasText(tokenApp)
                    || !StringUtils.hasText(requestApp)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Application identity missing");
                return;
            }

            tokenApp = tokenApp.trim().toUpperCase();
            requestApp = requestApp.trim().toUpperCase();

            if (!PHD.equals(tokenApp)
                    && !CMSEXAM.equals(tokenApp)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid application in token");
                return;
            }

            // 3. Compare JWT application with request header
            if (!tokenApp.equals(requestApp)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Application mismatch");
                return;
            }

            // 4. Check whether URL belongs to this application
            path = getApplicationPath(request);

            boolean phdPath = isApplicationPath(
                    path, "/cmsexam/phd/api", "/phd/api");

            boolean examPath = isApplicationPath(
                    path, "/cmsexam/exam/api", "/exam/api");

            // Shared authentication endpoints are allowed here.
            // Other shared endpoints must be explicitly classified.
            boolean sharedAuthPath =
                    isApplicationPath(
                            path,
                            "/cmsexam/api/auth",
                            "/api/auth");
            
            boolean sharedUserPath = isApplicationPath(
                    path, "/cmsexam/api/users", "/api/users");

            boolean correctPath =
                    (PHD.equals(tokenApp) && phdPath)
                    || (CMSEXAM.equals(tokenApp) && examPath)
                    || sharedAuthPath
                    || sharedUserPath;

            if (!correctPath) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access to this application API denied");
                return;
            }

            // 5. Load authenticated user
            String username =
                    jwtUtils.getUserNameFromJwtToken(jwt);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (!(userDetails instanceof UserDetailsImpl)) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid user authentication");
                return;
            }

            UserDetailsImpl userPrincipal =
                    (UserDetailsImpl) userDetails;

            // 6. Verify that user still has application permission
            boolean hasAccess =
                    userApplicationAccessRepository
                            .existsByUser_IdAndApplicationCode(
                                    userPrincipal.getId(),
                                    tokenApp);

            if (!hasAccess) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "User is not authorized for this application");
                return;
            }
            
            System.out.println("Request: " + request.getRequestURI());

            System.out.println("Application: " + tokenApp);

            userPrincipal.getAuthorities().forEach(a ->
                System.out.println("Granted Authority: " + a.getAuthority())
            );

            // 7. Set Spring Security authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            logger.error(
                    "Cannot set user authentication: {}",
                    e.getMessage(), e);

            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth =
                request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth)
                && headerAuth.startsWith("Bearer ")) {

            return headerAuth.substring(7).trim();
        }

        return null;
    }

    private String getApplicationPath(
            HttpServletRequest request) {

        String path = request.getRequestURI();

        String contextPath = request.getContextPath();

        if (StringUtils.hasText(contextPath)
                && path.startsWith(contextPath)) {

            path = path.substring(contextPath.length());
        }

        return path;
    }

    private boolean isApplicationPath(
            String path,
            String fullPrefix,
            String contextRelativePrefix) {

        return matchesPrefix(path, fullPrefix)
                || matchesPrefix(path, contextRelativePrefix);
    }

    private boolean matchesPrefix(
            String path, String prefix) {

        return path.equals(prefix)
                || path.startsWith(prefix + "/");
    }
}

