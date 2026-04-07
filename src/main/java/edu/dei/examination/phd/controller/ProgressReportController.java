package edu.dei.examination.phd.controller;


import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;
import edu.dei.examination.phd.service.ProgressReportService;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.ProgressReportRequest;
import edu.dei.examination.phd.dto.ProgressReportResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
public class ProgressReportController {

    private final ProgressReportService progressReportService;
    private final JwtUtils jwtUtils;

    public ProgressReportController(
            ProgressReportService service,
            JwtUtils jwtUtils) {
        this.progressReportService = service;
        this.jwtUtils = jwtUtils;
    }

    /* -------- Save Draft -------- */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveDraft(
            @RequestBody ProgressReportRequest request){
    
            //@RequestHeader("Authorization") String authHeader) {

        //Claims claims = jwtUtils.getClaims(authHeader);
        //Integer userId = claims.get("userId", Integer.class);
        
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

            UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

            Integer userId = userDetails.getId().intValue();

            progressReportService.saveDraft(userId, request);

        return ResponseEntity.ok(
                ApiResponse.success("Draft saved successfully", null)
        );
    }

    /* -------- Submit -------- */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<?>> submit(
            @RequestBody ProgressReportRequest req){
            
    	 
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

            UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();
            String username =userDetails.getUsername();
            

            Integer userId = userDetails.getId().intValue();
            progressReportService.submitReport(userId,username, req);

        return ResponseEntity.ok(
                ApiResponse.success("Report submitted successfully", null)
        );
    }
    
    @GetMapping("/semester/{semesterRegistrationId}")
    public ResponseEntity<ApiResponse<?>> getReportBySemester(
            @PathVariable Integer semesterRegistrationId) {

    	 Authentication authentication =
                 SecurityContextHolder.getContext().getAuthentication();

             UserDetailsImpl userDetails =
                 (UserDetailsImpl) authentication.getPrincipal();

             Integer userId = userDetails.getId().intValue();
        

        ProgressReportResponse response =
                progressReportService.getReportBySemester(userId, semesterRegistrationId);

        return ResponseEntity.ok(
                ApiResponse.success("Report fetched successfully", response)
        );
    }
    
//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<?>> getAllReports(
//            @RequestHeader("Authorization") String authHeader) {
//
//        Claims claims = jwtUtils.getClaims(authHeader);
//        Integer userId = claims.get("userId", Integer.class);
//
//        List<ProgressReportResponse> reports =
//                service.getAllReports(userId);
//
//        return ResponseEntity.ok(
//                ApiResponse.success("Reports fetched successfully", reports)
//        );
//    }


}
