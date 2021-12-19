package edu.dei.examination.cmsexm.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;
import edu.dei.examination.cmsexm.service.VerificationAgencyReferencesService;


@RestController
@RequestMapping("/api")
public class VerificationAgencyReferenceController {
	
	
	private VerificationAgencyReferencesService  referenceService;
	

	@Autowired
	public VerificationAgencyReferenceController(VerificationAgencyReferencesService theVerificationAgencyReferencesService) {
		
		this.referenceService = theVerificationAgencyReferencesService;
	}
	
	
	
	
	@GetMapping("/verificationagencyreference")
	public List<VerificationAgencyReferences> findAll() {
		return referenceService.findAll();
		
	}
	
	
	
	@GetMapping("/verificationagencyreference/{referenceId}")
	public VerificationAgencyReferences getVerificationAgency(@PathVariable int referenceId) {
		
		VerificationAgencyReferences thereferences = referenceService.findById(referenceId);
		
		if (thereferences == null) {
			throw new RuntimeException("Verification Agency  References not found - " + referenceId);
		}
		
		return thereferences;
	}
	
	// add mapping for POST /VerificationAgencyReference - add new VerificationAgencyreference
	@PostMapping("/verificationagencyreference")
	public VerificationAgencyReferences addVerificationAgencyReferences(@RequestBody VerificationAgencyReferences thereferences) {
		
		// also just in case they pass an id in JSON ... set id to 0
		// this is to force a save of new item ... instead of update
		if(thereferences.getId()>0) 
		thereferences.setId(0);
							
		referenceService.save(thereferences);
		
		return thereferences;
	}
	
	
	// add mapping for PUT /VerificationAgency - update existing VerificationAgency
	@PutMapping("/verificationagencyreference")
	public VerificationAgencyReferences updateVerificationAgency(@RequestBody VerificationAgencyReferences theVerificationAgencyReferences) {
		
		referenceService.save(theVerificationAgencyReferences);
		
		return theVerificationAgencyReferences;
	}
	
	
	
	@DeleteMapping("/verificationagencyreference/{referenceId}")
	public String deleteVerificationAgencyReferences(@PathVariable int referenceId) {
		
		VerificationAgencyReferences tempVerificationAgencyReferences = referenceService.findById(referenceId);
		
		// throw exception if null
		
		if (tempVerificationAgencyReferences == null) {
			throw new RuntimeException("Verification Agency  Reference id not found - " + referenceId);
		}
		
		referenceService.deleteById(referenceId);
		
		return "Deleted Verification Agency id - " + referenceId;
	}
	

	@DeleteMapping("/enrolment/{enrolmentId}")
	public String  deleteEnrolmentno(@PathVariable int enrolmentId) {
		referenceService.deletenEnrolmentno(enrolmentId);
		return "Deleted Enrolment Id - " + enrolmentId;
	}

	@GetMapping("/agencyreference/{agencyId}")
	public List<VerificationAgencyReferences>  agencyreferences(@PathVariable int agencyId) {
		return referenceService.findByAgencyid(agencyId);
		
	}

	@GetMapping("/agencyreferencebyprocessstatus")
	public List<VerificationAgencyReferences>  agencyprocessreferenceso(@RequestParam int agencyid,@RequestParam String processstatus  ) {
		return referenceService.findByProcessstatus(agencyid,processstatus);
		
	}
	
	@GetMapping("/agencyreferencepdf/{refId}")
	public String  genratepdf(@PathVariable int refId  ) {
		return referenceService.generateVerificationReport(refId);		
	}

}
