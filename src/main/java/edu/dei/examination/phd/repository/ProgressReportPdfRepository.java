package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.model.ProgressReportPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressReportPdfRepository
        extends JpaRepository<ProgressReportPdf, Integer> {

    List<ProgressReportPdf>
    findByReportIdOrderByVersionNoAsc(Integer reportId);

    Optional<ProgressReportPdf>
    findByReportIdAndVersionNo(Integer reportId, Integer versionNo);

    @Query("SELECT COALESCE(MAX(p.versionNo), 0) FROM ProgressReportPdf p " +
           " WHERE p.reportId = :reportId ")
           
    Integer findMaxVersionByReportId(
            @Param("reportId") Integer reportId);
}