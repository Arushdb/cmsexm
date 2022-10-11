package edu.dei.examination.cmsexm.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.model.Login;
import edu.dei.examination.cmsexm.model.UserRoles;
import edu.dei.examination.cmsexm.payload.request.LoginRequest;
import edu.dei.examination.cmsexm.payload.response.JwtResponse;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.cmsexm.service.UserDetailsServiceImpl;


//@CrossOrigin(origins = "localhost:4200", maxAge = 3600)


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;	

	



	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		Login login = new Login();
		
		
		
		List<UserRoles> dftroles  =userDetailsServiceImpl.getdefaultrole(userDetails.getId());
		
		//int id =dftroles.get(0).getUserrolePK().getUser_id();
		int roleid = dftroles.get(0).getUserrolePK().getRole_id();
		
		JSONArray menuary =userDetailsServiceImpl.getNewMenu(roleid);
		
		

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 menuary.toString(),roles));
	}
}
