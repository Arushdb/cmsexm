package edu.dei.examination.cmsexm.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.model.VerificationAgency;
import edu.dei.examination.cmsexm.model.VerificationAgencyReferences;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.cmsexm.service.VerificationAgencyService;

@RestController
@RequestMapping("/api")
public class VerificationAgencyController {
	
	
	private VerificationAgencyService  verificationAgencyService;

	@Autowired
	public VerificationAgencyController(VerificationAgencyService verificationAgencyService) {
		
		this.verificationAgencyService = verificationAgencyService;
	}
	
	
	@GetMapping("/verificationagency")
	public List<VerificationAgency> findAll() {
		return verificationAgencyService.findAll();
		
	}
	
	
	
	@GetMapping("/verificationagency/{verificationagencyId}")
	public VerificationAgency getVerificationAgency(@PathVariable int verificationagencyId) {
		
		VerificationAgency theVerificationAgency = verificationAgencyService.findById(verificationagencyId);
		
		if (theVerificationAgency == null) {
			throw new RuntimeException("Verification Agency not found - " + verificationagencyId);
		}
		
		return theVerificationAgency;
	}
	
	// add mapping for POST /VerificationAgency - add new VerificationAgency
	@PostMapping("/verificationagency")
	public VerificationAgency addVerificationAgency(@RequestBody VerificationAgency theVerificationAgency) {
		
		// also just in case they pass an id in JSON ... set id to 0
		// this is to force a save of new item ... instead of update
		
		theVerificationAgency.setId(0);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userdetail =(UserDetailsImpl)auth.getPrincipal();
		
				
		theVerificationAgency.setCreator_id(userdetail.getUsername());
		theVerificationAgency.setInsertime(new Date());
		
	
		
		
		verificationAgencyService.save(theVerificationAgency);
		
		return theVerificationAgency;
	}
	
	
	// add mapping for PUT /VerificationAgency - update existing VerificationAgency
	@PutMapping("/verificationagency")
	public VerificationAgency updateVerificationAgency(@RequestBody VerificationAgency theVerificationAgency) {
		
		verificationAgencyService.save(theVerificationAgency);
		
		return theVerificationAgency;
	}
	
	
	
	@DeleteMapping("/verificationagency/{verificationagencyId}")
	public String deleteVerificationAgency(@PathVariable int verificationagencyId) {
		
		VerificationAgency tempVerificationAgency = verificationAgencyService.findById(verificationagencyId);
		
		// throw exception if null
		
		if (tempVerificationAgency == null) {
			throw new RuntimeException("Verification Agency id not found - " + verificationagencyId);
		}
		
		verificationAgencyService.deleteById(verificationagencyId);
		
		return "Deleted Verification Agency id - " + verificationagencyId;
	}
	
	
//	@PostMapping("/verificationagencyref")
//	public VerificationAgency addverificationagencyref(@RequestBody VerificationAgencyReferences theVerificationAgencyReferences) {
//		
//		// also just in case they pass an id in JSON ... set id to 0
//		// this is to force a save of new item ... instead of update
//		
//		
//		
//		verificationAgencyService.save(theVerificationAgency);
//		
//		return theVerificationAgency;
//	}
	
	
	@GetMapping("/authenticverificationagency/{status}")
	public List<VerificationAgency> findByAuthentic(@PathVariable Boolean status) {
		return verificationAgencyService.findByAuthentic(status);
		
	} 

}
