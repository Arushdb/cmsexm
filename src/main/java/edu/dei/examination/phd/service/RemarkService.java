package edu.dei.examination.phd.service;



import edu.dei.examination.phd.model.ReviewerRemark;
import edu.dei.examination.phd.repository.ReviewerRemarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RemarkService {

    private final ReviewerRemarkRepository remarkRepo;

    public RemarkService(ReviewerRemarkRepository remarkRepo) { this.remarkRepo = remarkRepo; }

    public List<ReviewerRemark> findRemarks(String context, Integer contextId) {
        return remarkRepo.findByReviewContextAndContextIdOrderByRemarkDateAsc(context, contextId);
    }

    @Transactional
    public ReviewerRemark addRemark(ReviewerRemark r) {
        // Basic validation
        if (r.getReviewContext() == null || r.getContextId() == null || r.getRemarkText() == null) {
            throw new IllegalArgumentException("Invalid remark payload");
        }
        // optionally: check that context exists (e.g., progress report id exists) — implement in service if desired
        return remarkRepo.save(r);
    }

	public List<ReviewerRemark> getRemarksForScholar(Integer contextId) {
		// TODO Auto-generated method stub
		return remarkRepo.findByReviewContextAndContextIdAndIsPrivateFalseAndIsDeletedFalse("PROGRESS_REPORT", contextId);
	}
}
