package edu.dei.examination.cmsexm.service;

import java.util.List;

import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;

public interface VerificationAgencyReferencesService {
	
	
	public List<VerificationAgencyReferences> findAll();
	
	public VerificationAgencyReferences findById(int theId);
	
	public void save(VerificationAgencyReferences theverificationAgency);
	
	public void deleteById(int theId);
	
	



	public void deletenEnrolmentno(int theId );

}
