package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.model.ProgressReviewPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressReviewPolicyRepository 
        extends JpaRepository<ProgressReviewPolicy, Integer> {

    // ✅ Get full workflow for a program (MOST IMPORTANT)
    List<ProgressReviewPolicy> 
        findByProgramIdOrderBySequenceNo(Integer programId);

}