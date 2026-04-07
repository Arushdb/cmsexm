package edu.dei.examination.phd.controller;


import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.RemarkRequest;
import edu.dei.examination.phd.model.ReviewerRemark;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.model.Supervisor;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SupervisorRepository;
import edu.dei.examination.phd.service.RemarkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/remarks")
@CrossOrigin(origins = "*")
public class RemarkController {

    private final RemarkService svc;
    @Autowired
    private RoleRepository roleRepository;
    
    
    

    public RemarkController(RemarkService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<ReviewerRemark>> list(@RequestParam String context, @RequestParam Integer contextId) {
        return ResponseEntity.ok(svc.findRemarks(context, contextId));
    }

    // only supervisors/co-supervisors/HOD/DEAN allowed to add remarks
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR','CO_SUPERVISOR','HOD','DEAN','ADMIN','SCHOLAR')")
    public ResponseEntity<?> add(@Valid @RequestBody RemarkRequest req, BindingResult br, Principal principal) {
//        if (br.hasErrors()) {
//            return ResponseEntity.badRequest().body(br.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
//        }
        // map DTO -> entity
    	
    	 Authentication auth =
                 SecurityContextHolder.getContext().getAuthentication();

         UserDetailsImpl user =
                 (UserDetailsImpl) auth.getPrincipal();
         
         
      // ✅ Get first role
         String roleName = user.getAuthorities()
                           .stream()
                           .map(GrantedAuthority::getAuthority)
                           .filter(r -> r.contains("SUPERVISOR") 
                        		   || r.contains("CO_SUPERVISOR")
                                   || r.contains("HOD") 
                                   || r.contains("DEAN")
                                   || r.contains("SCHOLAR"))
                           .findFirst()
                           .orElse(null);
         
                  
         // 🚨 Restriction for scholar
         if ("ROLE_SCHOLAR".equals(roleName) && req.getParentRemarkId() == null) {
             throw new RuntimeException("Scholar cannot create new remark. Only reply allowed.");
         }
        
         
//         ScholarSemester ss = scholarSemesterRepository
//         		.findByScholarIdAndSemesterId(report.getScholarId(), report.getSemesterRegistrationId())
//                 
//                 .orElseThrow(() -> new RuntimeException("Semester data not found"));
         
        
         
         ERole roleEnum = ERole.valueOf(roleName);
         Role role = roleRepository.findByName(roleEnum)
       	        .orElseThrow(() -> new RuntimeException("Role not found"));
         
        
         // if it is reply then reviewer id should be  who started remarks.
         
         
    	
        ReviewerRemark r = new ReviewerRemark();
        r.setReviewContext(req.getReviewContext());
        r.setContextId(req.getContextId());
        r.setRemarkText(req.getRemarkText());
        // set reviewerRole from principal roles (optional override)
        r.setRole(role);;
        r.setIsPrivate(Boolean.TRUE.equals(req.getIsPrivate()));
        r.setIsDeleted(false);
        // optionally set reviewerId if principal maps to a supervisor record
        r.setParentRemarkId(req.getParentRemarkId());
        
         r.setUserid(user.getId().intValue());

        ReviewerRemark created = svc.addRemark(r);
        return ResponseEntity.ok(created);
    }
    
    
    @GetMapping("/scholar/{contextId}")
    @PreAuthorize("hasRole('ROLE_SCHOLAR')")
    public ResponseEntity<?> getRemarksForScholar(@PathVariable Integer contextId) {
   	
    	Authentication auth =
             SecurityContextHolder.getContext().getAuthentication();

     UserDetailsImpl user =
             (UserDetailsImpl) auth.getPrincipal();
     
     
    	

        List<ReviewerRemark> remarks = svc.getRemarksForScholar(contextId);

        return ResponseEntity.ok(remarks);
    }
    
}

