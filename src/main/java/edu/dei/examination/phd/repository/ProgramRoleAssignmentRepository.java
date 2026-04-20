package edu.dei.examination.phd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.phd.model.ProgramRoleAssignment;

public interface ProgramRoleAssignmentRepository extends JpaRepository<ProgramRoleAssignment, Integer> {

	List<ProgramRoleAssignment> findByProgram_ProgramId(Integer programId);

	List<ProgramRoleAssignment> findByUserId(Integer userId);

	List<ProgramRoleAssignment> findByRole(String role);

	List<ProgramRoleAssignment> findByProgram_ProgramIdAndRole(Integer programId, String role);
	
	


	@Query(" SELECT p FROM ProgramRoleAssignment p " + " JOIN p.program pr  JOIN p.user u  WHERE p.role = :role"
			+ " AND (LOWER(pr.programname) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ " OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')))"

	)
	List<ProgramRoleAssignment> searchByKeywordAndRole(@Param("keyword") String keyword, @Param("role") String role);

	@Query(
		   " SELECT p FROM ProgramRoleAssignment p "+
		   " JOIN FETCH p.user " +
		   " JOIN FETCH p.program " 
		   
		)
		List<ProgramRoleAssignment> findAllWithDetails();

}