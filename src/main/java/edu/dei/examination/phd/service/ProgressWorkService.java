package edu.dei.examination.phd.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

    private final ProgressWorkRepository repository;
    

    public ProgressWorkService(ProgressWorkRepository repository) {
        this.repository = repository;
    }

    @Transactional(transactionManager = "phdTransactionManager")
    public void saveProgressWork(
            Integer reportId,
            List<ProgressWork> rows) {
    	
    	 Authentication auth =
                 SecurityContextHolder.getContext().getAuthentication();

         UserDetailsImpl user =
                 (UserDetailsImpl) auth.getPrincipal();
         String username=user.getUsername();

        //repository.deleteByReportId(reportId);

        List<ProgressWork> entities = new ArrayList<>();

        for (ProgressWork dto : rows) {

            ProgressWork work = new ProgressWork();

            work.setReportId(reportId);
            work.setProgressWorkId(dto.getProgressWorkId());
            work.setCreatedBy(username);
            work.setStageOfResearch(dto.getStageOfResearch());
            work.setObjectiveNo(dto.getObjectiveNo());
            work.setCompletionPercentage(
                    dto.getCompletionPercentage());

            entities.add(work);
        }

        repository.saveAll(entities);
    }

    public List<ProgressWork> getByReportId(
            Integer reportId) {

        return repository.findByReportId(reportId);
    }
    
    @Transactional
    public void deleteProgressWork(int id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                "Progress Work not found. Id = " + id
            );
        }

        repository.deleteById(id);
    }
}