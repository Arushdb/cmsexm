package edu.dei.examination.phd.controller;


import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.phd.dto.ActiveSemesterDTO;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.service.ScholarRegistrationService;
import io.jsonwebtoken.Claims;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {
	
	@Autowired
	JwtUtils jwtUtils;

    private final ScholarRegistrationService scholarregService;
   
    

    public RegistrationController(ScholarRegistrationService regSvc) {
    	this.scholarregService = regSvc; }

  //  @PostMapping("/from-admission")
//    public ResponseEntity<?> registerFromAdmission(@RequestParam Integer admissionId,
//                                                   @RequestParam(defaultValue = "ABC") String univCode,
//                                                   @RequestParam(defaultValue = "PhD") String programCode) {
//        try {
//            //Scholar created = regSvc.createScholarFromAdmissionWithRetry(admissionId, univCode, programCode);
//           // return ResponseEntity.ok(created);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    
  
   
    
    @GetMapping("/active-semester")
    public ResponseEntity<ApiResponse<ActiveSemesterDTO>> getActiveSemester(
            @RequestHeader("Authorization") String authHeader) {

        // 1️⃣ Extract JWT claims
        Claims claims = jwtUtils.getClaims(authHeader);
        Integer userId = claims.get("userId", Integer.class);

        // 2️⃣ Get active semester ONCE
        Optional<ActiveSemesterDTO> activeSemesterOpt =
                scholarregService.getActiveSemester();

        if (!activeSemesterOpt.isPresent()) {
            return ResponseEntity.ok(
                    ApiResponse.success(
                            "There is no semester available for registration",
                            null
                    )
            );
        }

        ActiveSemesterDTO activeSemester = activeSemesterOpt.get();

        // 3️⃣ Validate scholar eligibility
        Scholars sch =
                scholarregService.validateScholar(
                        userId,
                        activeSemester.getSemesterId()
                );
        activeSemester.setScholarid(sch.getScholarId());

//        if (!canRegister) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(
//                            ApiResponse.error(
//                                    "Scholar already registered or previous semester not approved",null
//                            )
//                    );
//        }

        // 4️⃣ Success
        return ResponseEntity.ok(
                ApiResponse.success("success", activeSemester)
        );
    }

    @GetMapping("/register-semester")
    public ResponseEntity<ApiResponse<?> >registerSemester(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("scholarid")String scholarid,
            @RequestParam("semesterId")String semesterId
            
               		
    		){
    	
    	
    	System.out.println("semid"+semesterId+":scholarid"+scholarid);
    	return null;
    	
    }
    
    
    {
    	
    	
    	
    	
    }
    
    

}
