package edu.dei.examination.cmsexm.controller;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.model.Login;
import edu.dei.examination.cmsexm.model.RefreshToken;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.model.UserRoles;
import edu.dei.examination.cmsexm.payload.request.LoginRequest;
import edu.dei.examination.cmsexm.payload.request.ResetPasswordRequest;
import edu.dei.examination.cmsexm.payload.response.JwtResponse;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserApplicationAccessRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.cmsexm.service.PasswordService;
import edu.dei.examination.cmsexm.service.RefreshTokenService;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.cmsexm.service.UserDetailsServiceImpl;


//@CrossOrigin(origins = "localhost:4200", maxAge = 3600)


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"}, allowCredentials = "true") 
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;	
	
	 @Autowired
	    private PasswordService passwordService;
	 
	 @Autowired
	 private UserApplicationAccessRepository userApplicationAccessRepository;


	@PostMapping("/signin")	
	public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest
			, @RequestHeader("X-APPLICATION-ID") String applicationId) {
		
		 // Step 1: Validate application
	    if (!"PHD".equalsIgnoreCase(applicationId)
	            && !"CMSEXAM".equalsIgnoreCase(applicationId)) {

	        return ResponseEntity.badRequest()
	                .body(Collections.singletonMap(
	                        "message", "Invalid application"));
	    }
	    
	    String appCode = applicationId.trim().toUpperCase();
	  
	    
	    // Step 2: Resolve username
	    String resolvedUsername =
	            userDetailsServiceImpl.resolveUsername(
	                    loginRequest.getUsername());
	    
	    
	    // Step 3: Authenticate username and password
	    Authentication authentication =
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(
	                            resolvedUsername,
	                            loginRequest.getPassword()));
	    
	    // Step 4: Get authenticated user details
	    UserDetailsImpl userDetails =
	            (UserDetailsImpl) authentication.getPrincipal();

	    // Step 5: Retrieve user from database
	    User user = userRepository.findById(userDetails.getId())
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));
	    
	    // Step 6: Check application permission
	    boolean hasAccess =
	            userApplicationAccessRepository
	                    .existsByUser_IdAndApplicationCode(
	                            user.getId(), appCode);


	  

	    if (!hasAccess) {
	        return ResponseEntity.status(403).body(
	            Collections.singletonMap(
	                "message",
	                "You are not authorized to access this application"
	            )
	        );
	    }
	 // Step 7: Set authentication context
	    SecurityContextHolder.getContext()
	            .setAuthentication(authentication);

	    // Step 8: Generate application-specific access token
	    String jwt = jwtUtils.generateAccessToken(
	            userDetails, appCode);
	    
		
		


		
		// Generate and SAVE refresh token in database
		RefreshToken refreshToken =
		        refreshTokenService.createRefreshToken(
		                user, appCode);
		
		//UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		
		
		
		List<UserRoles> dftroles  =userDetailsServiceImpl.getdefaultrole(userDetails.getId());
		
		//int id =dftroles.get(0).getUserrolePK().getUser_id();
		int roleid = dftroles.get(0).getUserrolePK().getRole_id();
		
		JSONArray menuary =userDetailsServiceImpl.getNewMenu(roleid);
		
		String cookieName = "PHD".equalsIgnoreCase(appCode)
		        ? "PHD_REFRESH_TOKEN"
		        : "CMSEXAM_REFRESH_TOKEN";

		ResponseCookie cookie = ResponseCookie.from(
		        cookieName, refreshToken.getToken())
		        .httpOnly(true)
		        .secure(false)  // true in production HTTPS
		        .path("/")
		        .maxAge(Duration.ofDays(7))
		        .sameSite("Lax")
		        .build();

		
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).
				body(new JwtResponse(jwt, 
						 userDetails.getId(), 
						 userDetails.getUsername(),
						 
						 
						 menuary.toString(),roles,userDetails.getName()));


		
		
	}
	

@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(
        @RequestHeader("X-APPLICATION-ID") String applicationId,
        @CookieValue(value = "PHD_REFRESH_TOKEN", required = false)
        String phdRefreshToken,
        @CookieValue(value = "CMSEXAM_REFRESH_TOKEN", required = false)
        String cmsRefreshToken,
        HttpServletResponse response) {

    if (!"PHD".equalsIgnoreCase(applicationId)
            && !"CMSEXAM".equalsIgnoreCase(applicationId)) {
        return ResponseEntity.badRequest().body(
                Collections.singletonMap("message", "Invalid application"));
    }
    
    String appCode = applicationId.trim().toUpperCase();

    String refreshTokenValue =
            "PHD".equalsIgnoreCase(appCode)
                    ? phdRefreshToken : cmsRefreshToken;

    if (refreshTokenValue == null || refreshTokenValue.trim().isEmpty()) {
        return ResponseEntity.status(401).body(
                Collections.singletonMap("message", "Refresh token missing"));
    }

    // Verify token and application identity
    RefreshToken oldToken =
            refreshTokenService.verify(refreshTokenValue, appCode);

    // Rotate refresh token
    RefreshToken newToken =
            refreshTokenService.rotate(oldToken);

    User user = oldToken.getUser();
    
    boolean hasAccess =
            userApplicationAccessRepository
                    .existsByUser_IdAndApplicationCode(
                            user.getId(), appCode);
    
    if (!hasAccess) {
        return ResponseEntity.status(403).body(
                Collections.singletonMap(
                        "message",
                        "You are not authorized to access this application"));
    }


    // Load complete user details
    UserDetailsImpl userDetails =
            (UserDetailsImpl) userDetailsServiceImpl
                    .loadUserByUsername(user.getUsername());

    // Generate new access JWT
    String newAccessToken =
            jwtUtils.generateAccessToken(userDetails, applicationId);

    // Set the appropriate application cookie
    String cookieName =
            "PHD".equalsIgnoreCase(applicationId)
                    ? "PHD_REFRESH_TOKEN"
                    : "CMSEXAM_REFRESH_TOKEN";

    ResponseCookie cookie = ResponseCookie.from(
            cookieName, newToken.getToken())
            .httpOnly(true)
            .secure(false) // true in production HTTPS
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Lax")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(
            Collections.singletonMap("accessToken", newAccessToken));
}
	
	
	
	//@PostMapping("/refresh")
//	public ResponseEntity<?> refreshToken1(
//	        @CookieValue("refreshToken") String refreshTokenValue,
//	        HttpServletResponse response) {
//		
//		System.out.println("refreshTokenValue:"+refreshTokenValue);
//
//	    RefreshToken oldToken =
//	        refreshTokenService.verify(refreshTokenValue);
//
//	    // OPTIONAL but recommended
//	    RefreshToken newToken =
//	        refreshTokenService.rotate(oldToken);
//	    User user =oldToken.getUser();
//	    
//
//	    String newAccessToken =
//	        jwtUtils.generateAccessTokenFromUsername(user.getUsername());
//
//	    // Update cookie with new refresh token
//	    ResponseCookie cookie = ResponseCookie.from(
//	            "refreshToken", newToken.getToken())
//	        .httpOnly(true)
//	        .secure(true)
//	        .path("/api/auth")
//	        .maxAge(Duration.ofDays(7))
//	        .sameSite("Strict")
//	        .build();
//
//	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//	   
//	    
//	    return ResponseEntity.ok(
//	    	    Collections.singletonMap("accessToken", newAccessToken)
//	    	);
//	}
//
	
	 // =========================
    // 📧 FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestParam String email) {

        passwordService.sendResetLink(email);

        
        return ResponseEntity.ok(
        	    Collections.singletonMap("message", "Reset link sent")
        	);
    }

    // =========================
    // 🔐 RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest req) {

        passwordService.resetPassword(
                req.getToken(),
                req.getPassword()
        );

        
        return ResponseEntity.ok(
        	    Collections.singletonMap("message", "Password updated")
        	);
        
    }
}
