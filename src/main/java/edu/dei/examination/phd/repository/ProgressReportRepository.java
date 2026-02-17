package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.ProgressReport;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Integer> {
    
    Optional<ProgressReport> findByScholarIdAndSemesterRegistrationId(
            Integer scholarId,
            Integer semesterRegistrationId
    );

    boolean existsByScholarIdAndSemesterRegistrationId(
            Integer scholarId,
            Integer semesterRegistrationId
    );

    List<ProgressReport> findByScholarId(Integer scholarId);
}
