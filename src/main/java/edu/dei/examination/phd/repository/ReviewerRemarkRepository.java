package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.ReviewerRemark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewerRemarkRepository extends JpaRepository<ReviewerRemark, Integer> {
    List<ReviewerRemark> findByReviewContextAndContextIdOrderByRemarkDateAsc(String reviewContext, Integer contextId);
}

