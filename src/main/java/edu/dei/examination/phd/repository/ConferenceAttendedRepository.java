package edu.dei.examination.phd.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.ConferenceAttended;

@Repository
public interface ConferenceAttendedRepository
        extends JpaRepository<ConferenceAttended, Integer> {

    List<ConferenceAttended> findByReportId(Integer reportId);
    
    Optional<ConferenceAttended> findByIdAndReportId(
            Integer id,
            Integer reportId
    );

}