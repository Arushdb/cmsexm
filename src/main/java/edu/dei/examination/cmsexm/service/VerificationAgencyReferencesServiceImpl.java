package edu.dei.examination.cmsexm.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;
import edu.dei.examination.cmsexm.repository.VerificationAgencyReferencesRepository;
import edu.dei.examination.cmsexm.repository.VerificationEnrolmentRepository;

@Service
public class VerificationAgencyReferencesServiceImpl implements VerificationAgencyReferencesService {
	
	private VerificationAgencyReferencesRepository referencesRepository;
	private VerificationEnrolmentRepository enrolmentrepository;
	
	
//	@Autowired
//	private EntityManager entityManager;
	
	@Autowired
	public VerificationAgencyReferencesServiceImpl(VerificationAgencyReferencesRepository referencesRepository,
			VerificationEnrolmentRepository enrolmentrepository
			
			) {
	
		this.referencesRepository = referencesRepository;
		this.enrolmentrepository = enrolmentrepository;
	}
	
	
//   @Autowired
//	public VerificationAgencyReferencesServiceImpl(VerificationEnrolmentRepository enrolmentrepository) {
//	
//	
//}



	@Override
	public List<VerificationAgencyReferences> findAll() {
		// TODO Auto-generated method stub
		return referencesRepository.findAll();
	}

	@Override
	public VerificationAgencyReferences findById(int theId) {
		
		Optional<VerificationAgencyReferences> result=    referencesRepository.findById(theId);
		
		VerificationAgencyReferences  theVerificationreference = null;
		
		if(result.isPresent()) {
			
			theVerificationreference = result.get();
			
		}else {
			
			throw new RuntimeException("Did not find Verification Agency - " + theId);
			
		}
			
		
		return  theVerificationreference ;
		
	}

	@Override
	public void save(VerificationAgencyReferences  theverificationreferences) {
		
		theverificationreferences.setInsert_time(new Date());
		theverificationreferences.setCreator_id("Arush");
		
		
		referencesRepository.save(theverificationreferences);
			
	}

	@Override
	public void deleteById(int theId) {
		referencesRepository.deleteById(theId);
		
	}

	
	public void  deletenEnrolmen(int theId) {
//		int enr = 142045;
//		int ref = 16;
//		Query theQuery=    entityManager.createQuery("delete from  Ver_enrolmentno where enrolmentno=:enrolment "
//				+ "and reference_id=:ref");
//		
//		
//		//Query theQuery=    entityManager.createQuery("delete from  Ver_enrolmentno where enrolmentno=:enrolment ");
//			
//		
//		theQuery.setParameter("enrolment", enr);
//		theQuery.setParameter("ref", ref);
//	
//		
//		theQuery.executeUpdate();
		
	//	enrolmentrepository.deleteById(theId);
		
	
		
	}


	@Override
	public void deletenEnrolmentno(int theId) {
		enrolmentrepository.deleteById(theId);
		
	}


	@Override
	public List<VerificationAgencyReferences> findByAgencyid(int id) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyid(id);
	}


	@Override
	public List<VerificationAgencyReferences> findByProcessstatus(int agencyid,String sts) {
		// TODO Auto-generated method stub
		return referencesRepository.findByAgencyidAndProcessstatus(agencyid, sts);
	}
	
	
	
	
}
