package edu.dei.examination.cmsexm.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.repository.VerificationAgencyRepository;

@Service
public class VerificationAgencyServiceImpl implements VerificationAgencyService {
	
	private VerificationAgencyRepository verificationAgencyRepository;
	
	@Autowired
	public VerificationAgencyServiceImpl(VerificationAgencyRepository verificationAgencyRepository) {
	
		this.verificationAgencyRepository = verificationAgencyRepository;
	}

	@Override
	public List<VerificationAgency> findAll() {
		// TODO Auto-generated method stub
		return verificationAgencyRepository.findAll();
	}

	@Override
	public VerificationAgency findById(int theId) {
		
		Optional<VerificationAgency> result=    verificationAgencyRepository.findById(theId);
		
		VerificationAgency  theVerificationAgency = null;
		
		if(result.isPresent()) {
			
			theVerificationAgency = result.get();
			
		}else {
			
			throw new RuntimeException("Did not find Verification Agency - " + theId);
			
		}
			
		
		return  theVerificationAgency ;
		
	}

	@Override
	public void save(VerificationAgency theverificationAgency) {
		
		theverificationAgency.setInsertime(new Date());
		theverificationAgency.setCreator_id("Arush");
		
		if (theverificationAgency.getRequestref() != null) {
				
		theverificationAgency.getRequestref().get(0).setCreator_id("arush123");
		theverificationAgency.getRequestref().get(0).setInsert_time(new Date());
		
		}
		verificationAgencyRepository.save(theverificationAgency);
			
	}

	@Override
	public void deleteById(int theId) {
		verificationAgencyRepository.deleteById(theId);
		
	}

	
	
}
