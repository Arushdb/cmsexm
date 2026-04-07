package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.Report;
import edu.dei.examination.phd.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Integer> {

    // Get supervisor using logged-in user
    Optional<Supervisor> findByUserId(Integer userId);
    
//    @Query(
//    		" SELECT r FROM Report r "+
//    		" JOIN ScholarSemester ss ON r.scholarSemesterId = ss.id "+
//    		" JOIN ScholarSupervisor sp ON sp.scholarId = ss.scholarId " +
//    		" WHERE sp.supervisorId = :userId " +
//    		" AND r.currentSequenceNo = 1 "
//    		)
//    		List<Report> findSupervisorReports(Integer userId);
//    
//    
//    
//    
//    @Query(
//    	   "	SELECT r FROM Report r " +
//    	   "	JOIN ReportReviewerAssignment ra "+
//    	   "	  ON ra.reportId = r.id "+
//    	   "  WHERE ra.reviewerId = :userId "+
//    	   " AND r.currentSequenceNo = 2 "
//    		)
//    		List<Report> findReviewerReports(@Param("userId") Integer userId);
//    		
//    		
//    		@Query(
//    			   "	SELECT r FROM Report r " +
//    			   "	JOIN DepartmentRoleAssignment dra" + 
//    			   "	  ON dra.departmentId = r.departmentId " +
//    			   "	WHERE dra.userId = :userId "+
//    			   "	AND dra.roleId = :roleId " +
//    			   " 	AND r.currentSequenceNo = 3 "
//    				)
//    				List<Report> findHodReports(
//    				    @Param("userId") Integer userId,
//    				    @Param("roleId") Integer roleId
//    				);
//    				
//    				
//    				@Query(
//    						" SELECT r FROM Report r "+
//    						" JOIN FacultyRoleAssignment fra " +
//    						"  ON fra.facultyId = r.facultyId "+
//    						" WHERE fra.userId = :userId "+
//    						" AND fra.roleId = :roleId " +
//    						" AND r.currentSequenceNo = 4"
//    						)
//    						List<Report> findDeanReports(
//    						    @Param("userId") Integer userId,
//    						    @Param("roleId") Integer roleId
//    						);

}