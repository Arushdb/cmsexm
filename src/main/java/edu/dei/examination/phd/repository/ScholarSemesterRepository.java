package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSemester.ReviewStatus;
import edu.dei.examination.phd.model.Scholars;

import java.util.List;
import java.util.Optional;

public interface ScholarSemesterRepository
        extends JpaRepository<ScholarSemester, Integer> {

    /* All semesters of a scholar */
    List<ScholarSemester> findByScholarScholarId(Integer scholarId);

    /* One scholar + one semester */
    Optional<ScholarSemester> findByScholarScholarIdAndSemesterSemesterId(
            Integer scholarId,
            Integer semesterId
    );

    /* Latest semester */
     
    Optional<ScholarSemester> findTopByScholarScholarIdOrderBySemesterSemesterIdDesc(
            Integer scholarId
    );

    Optional<ScholarSemester> findTopByScholarScholarIdAndReviewStatusNotOrderBySemesterSemesterIdDesc(
            Integer scholarId,
            ScholarSemester.ReviewStatus reviewStatus
    );
    /* Pending/Approved/Rejected */
    List<ScholarSemester> findByReviewStatus(ReviewStatus status);

    /* Low attendance */
    @Query(
        "SELECT s FROM ScholarSemester s " +
        "WHERE s.attendancePercentage < :min"
    )
    List<ScholarSemester> findLowAttendance(
            @Param("min") Double minAttendance
    );

	//Optional<Scholars> findByScholarIdAndSemesterId(Integer scholarId, int semid);
    
   
    
}
