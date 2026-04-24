package edu.dei.examination.cmsexm.controller;

import java.time.Duration;
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
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.cmsexm.service.PasswordService;
import edu.dei.examination.cmsexm.service.RefreshTokenService;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.cmsexm.service.UserDetailsServiceImpl;


//@CrossOrigin(origins = "localhost:4200", maxAge = 3600)


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") 
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


	@PostMapping("/signin")	
	public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {

		String resolvedUsername =
			    userDetailsServiceImpl.resolveUsername(
			        loginRequest.getUsername()
			    );
		//Authentication authentication = authenticationManager.authenticate(
		//		new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(resolvedUsername, loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		String refreshToken = jwtUtils.generateRefreshToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		Login login = new Login();
		
		
		
		List<UserRoles> dftroles  =userDetailsServiceImpl.getdefaultrole(userDetails.getId());
		
		//int id =dftroles.get(0).getUserrolePK().getUser_id();
		int roleid = dftroles.get(0).getUserrolePK().getRole_id();
		
		JSONArray menuary =userDetailsServiceImpl.getNewMenu(roleid);
		ResponseCookie cookie =ResponseCookie.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(24*60*60*7)
				//.sameSite("Lax")
				.sameSite("None")
			
				.build();
		
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).
				body(new JwtResponse(jwt, 
						 userDetails.getId(), 
						 userDetails.getUsername(), 
						 menuary.toString(),roles));

//		return ResponseEntity.ok(new JwtResponse(jwt, 
//												 userDetails.getId(), 
//												 userDetails.getUsername(), 
//												 menuary.toString(),roles))
//				.header(HttpHeaders.SET_COOKIE, cookie.toString()).
//				body("Logged in Successfully");
		
		
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(
	        @CookieValue("refreshToken") String refreshTokenValue,
	        HttpServletResponse response) {

	    RefreshToken oldToken =
	        refreshTokenService.verify(refreshTokenValue);

	    // OPTIONAL but recommended
	    RefreshToken newToken =
	        refreshTokenService.rotate(oldToken);
	    User user =oldToken.getUser();
	    

	    String newAccessToken =
	        jwtUtils.generateAccessTokenFromUsername(user.getUsername());

	    // Update cookie with new refresh token
	    ResponseCookie cookie = ResponseCookie.from(
	            "refreshToken", newToken.getToken())
	        .httpOnly(true)
	        .secure(true)
	        .path("/api/auth")
	        .maxAge(Duration.ofDays(7))
	        .sameSite("Strict")
	        .build();

	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

	    return ResponseEntity.ok(
	        Map.of("accessToken", newAccessToken)
	    );
	}

	
	 // =========================
    // 📧 FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestParam String email) {

        passwordService.sendResetLink(email);

        return ResponseEntity.ok(Map.of("message", "Reset link sent"));
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

        return ResponseEntity.ok(Map.of("message", "Password updated"));
        
    }
}
