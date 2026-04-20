package edu.dei.examination.cmsexm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.cmsexm.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);
	
	@Query("SELECT distinct u FROM User u LEFT JOIN FETCH u.roles WHERE LOWER(u.username) = LOWER(:username)")
	Optional<User> findByUsernameWithRoles(String username);

	
	@Query("SELECT  u FROM User u  JOIN FETCH u.roles r WHERE r.id = :id")
	Optional<List<User>> findByRoleIds(Integer id);
	
	@Query("SELECT u FROM User u WHERE " +
		       "LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
		       "LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
		       "LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))")
		List<User> searchUsers(@Param("q") String q);
}
