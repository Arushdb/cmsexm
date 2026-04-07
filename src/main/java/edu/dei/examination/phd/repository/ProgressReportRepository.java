package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.ProgressReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Integer> {
    
//    Optional<ProgressReport> findByScholarIdAndSemesterRegistrationId(
//            Integer scholarId,
//            Integer semesterRegistrationId
//    );

//    boolean existsByScholarIdAndSemesterRegistrationId(
//            Integer scholarId,
//            Integer semesterRegistrationId
//    );
//	Optional<ProgressReport> 
//	findByScholarSemesterId(Integer scholarSemesterId);
	
	Optional<ProgressReport> 
	findTopByScholarSemesterIdOrderByIdDesc(Integer scholarSemesterId);
	
	
  //  List<ProgressReport> findByScholarId(Integer scholarId);
    
//    @Query(
//    		" SELECT pr FROM ProgressReport pr 	JOIN ScholarSupervisor ss ON pr.scholarId = ss.scholarId"+
//    		" WHERE ss.supervisorId = :supervisorId AND ss.isActive = true AND pr.progressStatus = 'SUBMITTED'"
//    		)
//    		List<ProgressReport> findPendingReportsBySupervisor(Integer supervisorId);
    
}
