package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.model.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewHistoryRepository 
        extends JpaRepository<ReviewHistory, Integer> {

    // ✅ Get full history of a report (timeline)
    List<ReviewHistory> findByReportIdOrderByActedAtAsc(Integer reportId);

}