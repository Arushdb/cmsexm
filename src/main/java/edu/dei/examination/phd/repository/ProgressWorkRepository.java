package edu.dei.examination.phd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.ProgressWork;

@Repository
public interface ProgressWorkRepository
        extends JpaRepository<ProgressWork, Integer> {

    List<ProgressWork> findByReportId(Integer reportId);
    
    Optional<ProgressWork> findByIdAndReportId(
            Integer id, Integer reportId);


}