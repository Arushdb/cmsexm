package edu.dei.examination.cmsexm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByName(ERole name);

}
