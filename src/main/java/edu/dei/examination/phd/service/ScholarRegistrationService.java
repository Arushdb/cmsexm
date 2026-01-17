package edu.dei.examination.phd.service;



import edu.dei.examination.phd.dto.ActiveSemesterDTO;
import edu.dei.examination.phd.exception.ScholarValidationException;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.model.Semesters;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SemesterRepository; // assume exists


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScholarRegistrationService {

    private final ScholarSemesterRepository scholarSemesterrepo;
    private final ScholarsRepository scholarRepo;
    private final SemesterRepository semesterRepo;
    

    public ScholarRegistrationService(ScholarSemesterRepository repo, ScholarsRepository scholarRepo, SemesterRepository semesterRepo) {
        this.scholarSemesterrepo = repo;
        this.scholarRepo = scholarRepo;
        this.semesterRepo = semesterRepo;
    }

    


    @Transactional
    public ScholarSemester applyForSemester(Integer scholarId, Integer semesterId, String notes, String username) {
        // validate scholar
       // Scholar sch = scholarRepo.findById(scholarId).orElseThrow(() -> new IllegalArgumentException("Scholar not found: " + scholarId));

        // validate semester exists and is open for registration (optional field)
        semesterRepo.findById(semesterId).orElseThrow(() -> new IllegalArgumentException("Semester not found: " + semesterId));
        // optionally check semester.isActive or isOpenForRegistration
        
        
;
        // check existing
     scholarSemesterrepo.findByScholarIdAndSemesterId(scholarId, semesterId).ifPresent(r -> {
            throw new IllegalArgumentException("Already applied for this semester");
        });

        ScholarSemester sreg = new ScholarSemester();
//        sreg.setScholar(sch);
//        sreg.setSemesterId(semesterId);
//       
//        sreg.setCreatedBy(username);
//        sreg.setStatus("APPLIED");

        return scholarSemesterrepo.save(sreg);
    }

//    @Transactional
//    public void cancelApplication(Integer scholarId, Integer semesterId, String username) {
//        ScholarSemester reg = repo.findByScholar_ScholarIdAndSemesterId(scholarId, semesterId)
//                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
//        if (!"APPLIED".equals(reg.getStatus())) {
//            throw new IllegalArgumentException("Cannot cancel application in status: " + reg.getStatus());
//        }
//        reg.setStatus("CANCELLED");
//        reg.setUpdatedBy(username);
//        reg.setUpdatedOn(java.time.LocalDateTime.now());
//        repo.save(reg);
//    }
    
    
    public Optional <ActiveSemesterDTO> getActiveSemester() {
    	LocalDate today = LocalDate.now();
    	
    	
    	 List<Semesters> semesters =
    			 semesterRepo.findActiveSemester(today);
    	 
    	 	 
    	 
    	 
    	 if (semesters.isEmpty()) {
    	        return Optional.empty();
    	    }
    	 
    	     	 
    	 Semesters s = semesters.get(0);
    	 
    	 
    	 ActiveSemesterDTO dto = new ActiveSemesterDTO(
    			 s.getSemesterId(),
    			 "SUCCESS",
    		        s.getSemesterName(),
    		        true,
    		        "Registration is open",
    		        0
    		        
    		        
    		    );
    	
    	return Optional.of(dto);
    }
    
    
    public Optional<Scholars> getscholar(int userid) {
    	LocalDate today = LocalDate.now();
        Optional<Scholars> schid =scholarRepo.findByUserId(userid)  ;
         return  schid;
	    
}




	public Scholars validateScholar(int userid,int semid) {
		// TODO Auto-generated method stub
		// if already registered 
		Scholars sch =scholarRepo.findByUserId(userid).orElseThrow(() -> new IllegalArgumentException("Scholar not found: "));
	
		  scholarSemesterrepo.findByScholarIdAndSemesterId(sch.getScholarId(),semid)
				.ifPresent(ss->{
					
					throw new ScholarValidationException(
						    "Scholar already registered "
						);

				
	});
		  
		  ScholarSemester latestSemester =
			        scholarSemesterrepo
			            .findTopByScholarIdOrderBySemesterIdDesc(sch.getScholarId())
			            .orElse(null);

			if (latestSemester != null) {
			    if (latestSemester.getReviewStatus() != ScholarSemester.ReviewStatus.Approved) {
			    	
			    	throw new ScholarValidationException(
			    			  "Last semester  review is pending/rejected"
						);	
			    	
			      
			    }
			}
		  
		  return sch;
		  

}
}