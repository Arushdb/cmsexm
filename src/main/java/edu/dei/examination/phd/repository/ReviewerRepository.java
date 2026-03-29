package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.Scholars;

import java.util.List;

@Repository
public interface ReviewerRepository extends JpaRepository<Scholars, Integer> {

    @Query(
        " SELECT new edu.dei.examination.phd.dto.ReviewerDashboardDTO("+
        " sc.scholarId,sc.enrolmentno,sem.academicYear,sc.fullName,p.programName,pr.id,pr.progressStatus,pr.submittedAt)"+
    
        "  FROM Scholars sc JOIN ScholarSupervisor ss ON sc.scholarId = ss.scholarId"+
		"    LEFT JOIN ProgressReport pr ON pr.scholarId = sc.scholarId" +
		"    JOIN Program p ON sc.programId = p.programId " +
		"    JOIN Semesters sem ON sem.semesterId=pr.semesterRegistrationId "+
		"    WHERE ss.supervisorId = :supervisorId " +
		"    AND ss.isActive = true "+
		"    AND pr.id = (" +
		"    SELECT MAX(pr2.id)" +
		"    FROM ProgressReport pr2"+
		"    WHERE pr2.scholarId = sc.scholarId)"
		)
		    
   
    List<ReviewerDashboardDTO> getReviewerDashboard(Integer supervisorId);

}
