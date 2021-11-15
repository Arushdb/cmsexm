package edu.dei.examination.cmsexm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

}
