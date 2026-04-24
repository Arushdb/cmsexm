package edu.dei.examination.cmsexm.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.model.UserIdentifier;
import edu.dei.examination.cmsexm.payload.request.UserDTO;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserIdentifierRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
//import edu.dei.examination.phd.service.UserDTO;
//import edu.dei.examination.phd.service.UserRole;
//import edu.dei.examination.phd.service.UserRoleId;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserIdentifierRepository userIdentifierRepository;
	
	@Autowired private PasswordService passwordService;
	
	
    @Transactional
	public User createUser(UserDTO dto) {
			//String username, String password, List<Integer> roleIds) {
        
		User user = new User();
		user.setUsername(dto.getUsername());

		// 🔐 BCrypt encoding
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		String password = PasswordUtil.generatePassword(10);
		System.out.println(password);
		user.setPassword(passwordEncoder.encode(password));
		
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setPhone(dto.getPhone());
		

		Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
		
		user.setRoles(roles);
		

		 userRepository.save(user);
		 passwordService.sendResetLink(user.getEmail());
		 
		 return user;
		 
		 

	}

//	public void addUserIdentifier(Integer userId, UserIdentifier.IdentifierType type, String value) {
//
//		// ✅ Optional: check duplicate
//		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//		// ✅ Optional duplicate check
//		if (userIdentifierRepository.findByIdentifierValue(value) != null) {
//			throw new RuntimeException("Identifier already exists");
//		}
//
//		UserIdentifier identifier = new UserIdentifier();
//		identifier.setUser(user);
//		identifier.setIdentifierType(type);
//		identifier.setIdentifierValue(value);
//		identifier.setStatus(UserIdentifier.Status.ACTIVE);
//
//		userIdentifierRepository.save(identifier);
//	}

	public void addUserIdentifier(Integer  userId, UserIdentifier.IdentifierType type, String value) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		
//		Set<Integer> roleIds = user.getRoles().stream()
//		        .map(ur -> ur.getRole().getId())
//		        .collect(Collectors.toSet());
		
		
		boolean isScholar = user.getRoles().stream().anyMatch(r -> r.getName().equals(ERole.ROLE_SCHOLAR));
		
		
// 🔥 Only allow for SCHOLAR
		//boolean isScholar = user.getRoles().stream().anyMatch(r -> r.getName().equals("SCHOLAR"));
		
	

		if (!isScholar) {
			throw new RuntimeException("Identifiers allowed only for SCHOLAR");
		}

		UserIdentifier identifier = new UserIdentifier();
		identifier.setUser(user);
		identifier.setIdentifierType(type);
		identifier.setIdentifierValue(value);

		userIdentifierRepository.save(identifier);
	}
	
	 @Transactional
	    public User createUserold(UserDTO dto) {

	        if (userRepository.existsByUsername(dto.getUsername())) {
	            throw new RuntimeException("Username already exists");
	        }

	        User user = new User();
	        user.setUsername(dto.getUsername());
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	        user.setName(dto.getName());
	        user.setEmail(dto.getEmail());

	        user = userRepository.save(user);

	        for (Integer roleId : dto.getRoleIds()) {

	            Role role = roleRepository.findById(roleId)
	                    .orElseThrow(() -> new RuntimeException("Invalid role"));

	           // UserRole ur = new UserRole();

	            //UserRoleId id = new UserRoleId();
//	            id.setUserId(user.getId());
//	            id.setRoleId(roleId);
//
//	            ur.setId(id);
//	            ur.setUser(user);
//	            ur.setRole(role);
//	            ur.setDefaultRole(true);
//
//	            userRoleRepository.save(ur);
	        }

	        return user;
	    }

	    // =========================
	    // GET ALL USERS
	    // =========================
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    // =========================
	    // GET USER BY ID
	    // =========================
	    public User getUser(Integer id) {
	        return userRepository.findById(id).orElseThrow();
	    }

	    // =========================
	    // UPDATE USER
	    // =========================
	    public User updateUser(Integer id, UserDTO dto) {

	        User user = getUser(id);

	        user.setName(dto.getName());
	        user.setEmail(dto.getEmail());
	        user.setPhone(dto.getPhone());
	        Set<Role> roles =new HashSet<> (roleRepository.findAllById(dto.getRoleIds()));
	        
	        user.setRoles(roles);

	        return userRepository.save(user);
	    }

	    // =========================
	    // DELETE USER
	    // =========================
	    public void deleteUser(Integer id) {
	        userRepository.deleteById(id);
	    }
	    public List<Role> getroles() {
	    	return roleRepository.findAll();
	    }
	   
	    
	    public List<User> searchUsers(String keyword) {
	        return userRepository.searchUsers(keyword);
	    }
	    public List<User> getUserswithroles(String roles) {
	    	Role role =roleRepository.findByName(ERole.valueOf(roles)).orElseThrow(()->new RuntimeException("Roles not found"));
	    	
	    	List<User> list= userRepository.findByRoleIds(role.getId()).orElseThrow
    		(()->new RuntimeException("No user found with this role"+roles));
	    	
	        return userRepository.findByRoleIds(role.getId()).orElseThrow
	        		(()->new RuntimeException("No user found with this role"+roles));
	    }

	
}