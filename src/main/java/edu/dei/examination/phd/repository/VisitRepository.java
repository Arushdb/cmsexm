package edu.dei.examination.phd.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    List<Visit> findByReportId(Integer reportId);
    
    Optional<Visit> findByIdAndReportId(
            Integer id,
            Integer reportId
    );

}