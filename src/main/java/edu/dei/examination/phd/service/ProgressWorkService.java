package edu.dei.examination.phd.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ProgressWorkDto;
import edu.dei.examination.phd.model.ProgressWork;
import edu.dei.examination.phd.repository.ProgressWorkRepository;

@Service

public class ProgressWorkService {

    @Autowired
    private ProgressWorkRepository repository;

    public ProgressWork saveProgressWork(
            Integer reportId, ProgressWork work) {

        work.setReportId(reportId);
        work.setCreatedAt(LocalDateTime.now());

        return repository.save(work);
    }

    public List<ProgressWork> getProgressWork(Integer reportId) {
        return repository.findByReportId(reportId);
    }

    public Optional<ProgressWork> updateProgressWork(
            Integer id, Integer reportId, ProgressWork work) {

        return repository.findByIdAndReportId(id, reportId)
                .map(existing -> {
                    existing.setStage(work.getStage());
                    existing.setObjectiveNo(work.getObjectiveNo());
                    existing.setCompletionPercentage(
                            work.getCompletionPercentage());
                    existing.setUpdatedAt(LocalDateTime.now());

                    return repository.save(existing);
                });
    }

    public boolean deleteProgressWork(
            Integer id, Integer reportId) {

        return repository.findByIdAndReportId(id, reportId)
                .map(existing -> {
                    repository.delete(existing);
                    return true;
                })
                .orElse(false);
    }
}
