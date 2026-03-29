package edu.dei.examination.phd.controller;

import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.ReviewDetailDTO;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.service.ReviewerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviewer")
public class ReviewerController {

    private final ReviewerService reviewerService;

    public ReviewerController(ReviewerService reviewerService) {
        this.reviewerService = reviewerService;
    }

    // ===============================
    // 1️⃣ Dashboard (Scholars + Reports)
    // ===============================
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> getDashboard() {
    	
    	
    	
    	Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl user =
                (UserDetailsImpl) auth.getPrincipal();
        int userId =user.getId().intValue();

        //Integer userId = reviewerService.getLoggedInUserId();
        Integer supervisorId = reviewerService.getSupervisorIdByUser(userId);

        List<ReviewerDashboardDTO> data =
                reviewerService.getReviewerDashboard(supervisorId);
        
        return ResponseEntity.ok(
                ApiResponse.success("Reviewer dashboard data", data));
        

       
    }

    // ===============================
    // 2️⃣ Get Pending Reports
    // ===============================
//    @GetMapping("/pending")
//    public ResponseEntity<List<ProgressReport>> getPendingReports() {
//
//        Integer userId = reviewerService.getLoggedInUserId();
//        Integer supervisorId = reviewerService.getSupervisorIdByUser(userId);
//
//        List<ProgressReport> reports =
//                reviewerService.getPendingReports(supervisorId);
//
//        return ResponseEntity.ok(reports);
//    }

    // ===============================
    // 3️⃣ View Report Details
    // ===============================
    @GetMapping("/report/{reportId}")
    public ResponseEntity<ReviewDetailDTO> getReport(
            @PathVariable Integer reportId) {

    	 ReviewDetailDTO data = reviewerService.getReportDetails(reportId);

        return ResponseEntity.ok(data);
    }

    // ===============================
    // 4️⃣ Approve Report
    // ===============================
//    @PostMapping("/approve/{reportId}")
//    public ResponseEntity<String> approveReport(
//            @PathVariable Integer reportId) {
//
//        reviewerService.approveReport(reportId);
//
//        return ResponseEntity.ok("Report approved successfully");
//    }

//    // ===============================
//    // 5️⃣ Request Revision
//    // ===============================
//    @PostMapping("/revision/{reportId}")
//    public ResponseEntity<String> requestRevision(
//            @PathVariable Integer reportId,
//            @RequestParam String remarks) {
//
//        reviewerService.requestRevision(reportId, remarks);
//
//        return ResponseEntity.ok("Revision requested successfully");
//    }
    // ===============================
    // 3️⃣ Submit Review Decision
    // ===============================
    @PostMapping("/review")
    public ResponseEntity<String> reviewReport(
            @RequestParam Integer reportId,
            @RequestParam String decision,
            @RequestParam(required = false) String remarks) {

        reviewerService.reviewReport(reportId, decision, remarks);

        return ResponseEntity.ok("Review submitted successfully");
    }

}