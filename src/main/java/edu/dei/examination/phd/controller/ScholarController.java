package edu.dei.examination.phd.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.exception.ResourceNotFoundException;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.dto.ScholarDashboardDTO;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.service.ScholarService;
import io.jsonwebtoken.Claims;


@RestController
@RequestMapping("/api/scholar")
public class ScholarController {
	

	@Autowired
	JwtUtils jwtUtils;

    private final ScholarService scholarService;

    public ScholarController(ScholarService scholarService) {
        this.scholarService = scholarService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Integer>> generateScholars(@RequestBody CreateScholarsRequest request) {
        int count = scholarService.createScholarsForAcademicYearAndMonth(request);
    
        if (count==0) {
        	throw new ResourceNotFoundException("Record not found");
        	//return ResponseEntity.ok( ApiResponse.success("Created scholars successfully", count) );
        	
        }
        
        else { 
        		return ResponseEntity.ok( ApiResponse.success("No scholars created", count) );}
    }
    
    @GetMapping("/scholardashboard")
    
    
    public ResponseEntity<ApiResponse<ScholarDashboardDTO> > dashboard(@RequestHeader("Authorization") String authHeader) {
    	
    	 // 1️⃣ Extract JWT claims
        Claims claims = jwtUtils.getClaims(authHeader);
        String username = claims.getSubject();
        Integer userid = claims.get("userId", Integer.class);
        System.out.println(claims);
        
        
        Scholars scholar=scholarService.getScholarByUserid(userid);
        
        
        //Scholars scholar=scholarService.getScholarByUserid(1);
        
        
        //int semid  =Integer.parseInt(semesterId);
        //int schid  =Integer.parseInt(scholarid);

        ScholarDashboardDTO dto =
                scholarService.getScholarDashboard(scholar.getScholarId());
        
        ApiResponse<ScholarDashboardDTO> response =
                new ApiResponse<>(
                        true,
                        "Scholar profile fetched successfully",
                        dto,
                        null
                );
        
        return ResponseEntity.ok(response);

        
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getscholarProfile(@RequestHeader("Authorization") String authHeader) {
    	
    	 // 1️⃣ Extract JWT claims
        Claims claims = jwtUtils.getClaims(authHeader);
        String username = claims.getSubject();
        Integer userid = claims.get("userId", Integer.class);
        System.out.println(claims);
        
    	
    	Scholars scholar=scholarService.getScholarByUserid(userid);
    	
    	ScholarDashboardDTO dto =
                scholarService.getScholarDashboard(scholar.getScholarId());
    	 ApiResponse<ScholarDashboardDTO> response =
                 new ApiResponse<>(
                         true,
                         "Scholar profile fetched successfully",
                         dto,
                         null
                 );
         
         return ResponseEntity.ok(response);

        
    	
    	
    	         
    }
    
    
}

