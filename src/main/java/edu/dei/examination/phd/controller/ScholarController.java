package edu.dei.examination.phd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.exception.ResourceNotFoundException;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.service.ScholarService;


@RestController
@RequestMapping("/api/scholars")
public class ScholarController {

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
}

