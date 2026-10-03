package edu.dei.examination.phd.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.phd.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    //List<Publication> findByProgressReportId(Integer reportId);
	
	List<Publication> findByReportId(Integer reportId);
    
    Optional<Publication> findByIdAndReportId(
            Integer id, Integer reportId);
    

}