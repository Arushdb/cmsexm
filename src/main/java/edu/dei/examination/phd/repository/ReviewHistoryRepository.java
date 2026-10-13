package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.model.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewHistoryRepository 
        extends JpaRepository<ReviewHistory, Integer> {

    // ✅ Get full history of a report (timeline)
    List<ReviewHistory> findByReportIdOrderByActedAtAsc(Integer reportId);
    
    @Query(value =
    	    "SELECT rh.role_id, r.name, rh.action, rh.acted_at " +
    	    "FROM review_history rh " +
    	    "JOIN exam_live.roles r ON r.id = rh.role_id " +
    	    "WHERE rh.report_id = :reportId " +
    	    "ORDER BY rh.acted_at DESC",
    	    nativeQuery = true)
    	List<Object[]> findHistoryWithRoleNames(
    	        @Param("reportId") Integer reportId);
    	
    	@Query(value =
    		    "SELECT r.name AS role_name, " +
    		    "rh.action, " +
    		    "u.name AS approver_name " +
    		    "FROM review_history rh " +
    		    "JOIN exam_live.roles r ON r.id = rh.role_id " +
    		    "LEFT JOIN exam_live.users u ON u.id = rh.acted_by " +
    		    "WHERE rh.report_id = :reportId " +
    		    "ORDER BY rh.acted_at DESC",
    		    nativeQuery = true)
    		List<Object[]> findApprovalDetails(
    		    @Param("reportId") Integer reportId);

}