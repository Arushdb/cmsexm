package edu.dei.examination.cmsexm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.VerificationAgency;
import java.lang.String;
import java.util.List;

public interface VerificationAgencyRepository extends JpaRepository<VerificationAgency, Integer> {

	 	
	 List<VerificationAgency> findByAuthentic(Boolean a);
}
