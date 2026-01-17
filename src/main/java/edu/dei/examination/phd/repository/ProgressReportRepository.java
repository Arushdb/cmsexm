package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.ProgressReport;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Integer> {
    List<ProgressReport> findByScholar_ScholarIdOrderByCreatedAtDesc(Integer scholarId);
}
