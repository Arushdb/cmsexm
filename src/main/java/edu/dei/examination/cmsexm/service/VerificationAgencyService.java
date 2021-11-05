package edu.dei.examination.cmsexm.service;

import java.util.List;

import edu.dei.examination.cmsexm.model.VerificationAgency;

public interface VerificationAgencyService {
	
	
	public List<VerificationAgency> findAll();
	
	public VerificationAgency findById(int theId);
	
	public void save(VerificationAgency theverificationAgency);
	
	public void deleteById(int theId);

}
