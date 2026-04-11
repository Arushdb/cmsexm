package edu.dei.examination.phd.controller;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.service.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // ✅ Single API for all roles
    @GetMapping("/dashboard")
   // public ResponseEntity<List<ReviewerDashboardDTO>> getDashboard(Principal principal) {
    public ResponseEntity<ApiResponse<?>> getDashboard(Principal principal) {
    	
        String username = principal.getName();

        List<ReviewerDashboardDTO> data =
                reportService.getDashboardForUser(username);

        //return ResponseEntity.ok(data);
        return ResponseEntity.ok(
                ApiResponse.success("Reviewer dashboard data", data));
    }
    
    @PostMapping("/review")
    public ResponseEntity<ApiResponse<?>> processReview(
            @RequestParam Integer reportId,
            @RequestParam String decision ,
            @RequestParam String remarks
             // APPROVE / REJECT
    ) {
        reportService.processReview(reportId, remarks, decision);
        //return ResponseEntity.ok("c");
        return ResponseEntity.ok(
                ApiResponse.success("Reviewer processReview data", "success"));
    }
}