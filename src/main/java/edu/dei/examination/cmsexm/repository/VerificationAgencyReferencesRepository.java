package edu.dei.examination.cmsexm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;

public interface VerificationAgencyReferencesRepository extends JpaRepository<VerificationAgencyReferences, Integer> {

	List <VerificationAgencyReferences>findByAgencyid(int id);
	
	List <VerificationAgencyReferences>findByAgencyidAndProcessstatus(int id,String sts);

	
}
