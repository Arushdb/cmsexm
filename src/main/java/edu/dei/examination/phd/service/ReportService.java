package edu.dei.examination.phd.service;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.enums.ProgressStatus;
import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.Program;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ProgressReviewPolicy;
import edu.dei.examination.phd.model.Report;

import edu.dei.examination.phd.model.ReviewHistory;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.repository.ProgramRepository;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.ProgressReviewPolicyRepository;
import edu.dei.examination.phd.repository.ReportRepository;
import edu.dei.examination.phd.repository.ReviewHistoryRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.AccessOptions.SetOptions.Propagation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private ProgressReportRepository progressReportRepo;
	
	@Autowired
	private ScholarSemesterRepository scholarSemesterRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	
	@Autowired
	private ProgramRepository programRepo;
	
	@Autowired
	private ScholarSupervisorRepository scholarSupervisorRepo ;
	
		
	@Autowired
    private ProgressReviewPolicyRepository policyRepo;
	@Autowired
    private ReviewHistoryRepository  historyRepo;

	// 🔥 Main method
	public List<ReviewerDashboardDTO> getDashboardForUser(String username) {

		 Authentication auth =
                 SecurityContextHolder.getContext().getAuthentication();

         UserDetailsImpl user =
                 (UserDetailsImpl) auth.getPrincipal();
       
         Set<String> roles = user.getAuthorities()
        	        .stream()
        	        .map(GrantedAuthority::getAuthority)
        	        .collect(Collectors.toSet());
		
		
		// SUPERVISOR / REVIEWER / HOD / DEAN

         if (roles.contains("ROLE_SUPERVISOR")) {
        	    return reportRepository.getSupervisorDashboard(user.getId().intValue());
        	}

        	if (roles.contains("ROLE_REVIEWER")) {
        	    return reportRepository.getReviewerDashboard(user.getId().intValue());
        	}

        	if (roles.contains("ROLE_HOD")) {
        	    return reportRepository.getHodDashboard(user.getId().intValue());
        	}

        	if (roles.contains("ROLE_DEAN")) {
        	    return reportRepository.getDeanDashboard(user.getId().intValue());
        	}

        	throw new RuntimeException("No valid role found");
         
         
	}

	@Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, transactionManager = "phdTransactionManager")
	public Report createReport(String username,ProgressReport progressReport) {

		Report r=reportRepository.findByProgressReportId(progressReport.getId())
				.orElseGet(()-> new Report());
		//Report r = new Report());
		r.setProgressReportId(progressReport.getId());

		// reportRepository.findby
		r.setScholarSemester(progressReport.getScholarSemester());
		r.setProgramId(progressReport.getScholarSemester().getProgramId());
		r.setDepartmentId(progressReport.getScholarSemester().getDepartmentId());
//			        r.setFacultyId(ss.getFacultyId());

		// 🔥 IMPORTANT FIX
		Integer facultyId = progressReport.getScholarSemester().getFacultyId();
		if (facultyId == null) {
			throw new IllegalArgumentException("Faculty ID is missing");
		}

		r.setCurrentSequenceNo(1); // 🔥 start with Supervisor
		r.setStatus(ProgressStatus.SUBMITTED);

		r.setSubmittedOn(LocalDateTime.now());
		r.setCreatedAt(LocalDateTime.now());
		r.setCreatedBy(username);
		r.setFacultyId(facultyId);

		return reportRepository.save(r);
	}

//	 public void processReview(Integer reportId, Integer userRoleId,String action) {
//		 
//		 Authentication auth =
//                 SecurityContextHolder.getContext().getAuthentication();
//
//         UserDetailsImpl user =
//                 (UserDetailsImpl) auth.getPrincipal();
//
//	        Report report = reportRepository.findById(reportId).orElseThrow();
//
//	        List<ProgressReviewPolicy> policies =
//	                policyRepo.findByProgramIdOrderBySequenceNo(report.getProgramId());
//
//	        ProgressReviewPolicy current = policies.stream()
//	                .filter(p -> p.getSequenceNo().equals(report.getCurrentSequenceNo()))
//	                .findFirst()
//	                .orElseThrow();
//
//	        // ✅ Role validation
//	        if (!current.getRoleId().equals(userRoleId)) {
//	            throw new RuntimeException("Unauthorized action");
//	        }
//
//	        // 🔄 Find next step
//	        ProgressReviewPolicy next = policies.stream()
//	                .filter(p -> p.getSequenceNo() > current.getSequenceNo())
//	                .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
//	                .findFirst()
//	                .orElse(null);
//
//	        // 🔁 Skip optional
//	        while (next != null && Boolean.FALSE.equals(next.getIsMandatory())) {
//	            Integer nextSeq = next.getSequenceNo();
//
//	            next = policies.stream()
//	                    .filter(p -> p.getSequenceNo() > nextSeq)
//	                    .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
//	                    .findFirst()
//	                    .orElse(null);
//	        }
//
//	        // ✅ Update report
//	        if (next != null) {
//	            report.setCurrentSequenceNo(next.getSequenceNo());
//	            report.setStatus(ProgressStatus.UNDERREVIEW);
//	        } else {
//	            report.setStatus(ProgressStatus.APPROVED);
//	            report.setCurrentSequenceNo(null);
//	        }
//	        ReviewHistory h = new ReviewHistory();
//	        h.setReportId(report.getId());
//	        h.setRoleId(userRoleId);
//	        h.setAction("APPROVED");
//	        h.setActedBy(user.getId().intValue());
//	        h.setActedAt(LocalDateTime.now());
//
//	        historyRepo.save(h);
//	        reportRepository.save(report);
//}
//

//	 public void processReview(Integer reportId, String Remarks, String action) {
//
//		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
//
//		    //userRoleId=user.getAuthorities().forEach(null);
//		    
//		    String role = user.getAuthorities()
//		            .stream()
//		            .findFirst()
//		            .map(a -> a.getAuthority())
//		            .orElseThrow(() -> new RuntimeException("No role found"));
//		    
//		    Integer userRoleId = roleRepo.findByName(ERole.valueOf(role))
//		            .map(Role::getId)
//		            .orElseThrow(() -> new RuntimeException("Role not found"));
//		    
////		    Report report = reportRepository.findById(reportId)
////		            .orElseThrow(() -> new RuntimeException("Report not found"));
//		    Report report = reportRepository.findByProgressReportId(reportId)
//		            .orElseThrow(() -> new RuntimeException("Report not found"));
//		    
//
//		    List<ProgressReviewPolicy> policies =
//		            policyRepo.findByProgramIdOrderBySequenceNo(report.getProgramId());
//
//		    ProgressReviewPolicy current = policies.stream()
//		            .filter(p -> p.getSequenceNo().equals(report.getCurrentSequenceNo()))
//		            .findFirst()
//		            .orElseThrow(() -> new RuntimeException("Invalid workflow state"));
//
//		    // ✅ Role validation
//		    if (!current.getRoleId().equals(userRoleId)) {
//		        throw new RuntimeException("Unauthorized action");
//		    }
//
//		    // =========================
//		    // 🔴 HANDLE REJECT
//		    // =========================
//		    if ("REJECTED".equalsIgnoreCase(action)) {
//
//		        report.setStatus(ProgressStatus.REJECTED);
//		        report.setCurrentSequenceNo(null);
//
//		        // ✅ Update ProgressReport
//		        ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
//		        pr.setProgressStatus(ProgressStatus.REJECTED); 
//		       // (ProgressStatus.REJECTED);
//
//		        // ✅ Update ScholarSemester
//		        ScholarSemester ss = pr.getScholarSemester();
//		        ss.setReviewStatus(ScholarSemester.ReviewStatus.Rejected);
//
//		        progressReportRepo.save(pr);
//		        scholarSemesterRepo.save(ss);
//		    }
//
//		    // =========================
//		    // ✅ HANDLE APPROVE
//		    // =========================
//		    else if ("APPROVED".equalsIgnoreCase(action)) {
//
//		        // 🔄 Find next step
//		        ProgressReviewPolicy next = policies.stream()
//		                .filter(p -> p.getSequenceNo() > current.getSequenceNo())
//		                .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
//		                .findFirst()
//		                .orElse(null);
//
//		        // 🔁 Skip optional
//		        while (next != null && Boolean.FALSE.equals(next.getIsMandatory())) {
//		            Integer nextSeq = next.getSequenceNo();
//
//		            next = policies.stream()
//		                    .filter(p -> p.getSequenceNo() > nextSeq)
//		                    .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
//		                    .findFirst()
//		                    .orElse(null);
//		        }
//
//		        if (next != null) {
//		            // 👉 Move to next reviewer
//		            report.setCurrentSequenceNo(next.getSequenceNo());
//		            report.setStatus(ProgressStatus.UNDERREVIEW);
//		        } else {
//		            // 🎯 FINAL APPROVAL
//		            report.setStatus(ProgressStatus.APPROVED);
//		            report.setCurrentSequenceNo(null);
//
//		            // ✅ Update ProgressReport
//		            ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
//		            pr.setProgressStatus(ProgressStatus.APPROVED);
//
//		            // ✅ Update ScholarSemester
//		            ScholarSemester ss = pr.getScholarSemester();
//		            ss.setReviewStatus(ScholarSemester.ReviewStatus.Approved);
//
//		            progressReportRepo.save(pr);
//		            scholarSemesterRepo.save(ss);
//		        }
//		    }
//		    else if("REVISION_REQUIRED".equalsIgnoreCase(action)) {
//		    	 ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
//		            pr.setProgressStatus(ProgressStatus.REVISION_REQUIRED);
//		    
//              pr.setNextActions(Remarks);
//              progressReportRepo.save(pr);
//              
//		    	
//		    }
//
//		    else {
//		        throw new RuntimeException("Invalid action");
//		    }
//
//		    // =========================
//		    // 📝 Save History
//		    // =========================
//		    ReviewHistory h = new ReviewHistory();
//		    h.setReportId(report.getId());
//		    h.setRoleId(userRoleId);
//		    h.setAction(action);   // ✅ FIXED
//		    h.setActedBy(user.getId().intValue());
//		    h.setActedAt(LocalDateTime.now());
//		    h.setRemarks(Remarks);
//
//		    historyRepo.save(h);
//
//		    // =========================
//		    // 💾 Save Report
//		    // =========================
//		    reportRepository.save(report);
//		}
//	 
//	public void processReview(Integer reportId, String Remarks, String action) {
//
//	    Authentication auth = SecurityContextHolder
//	            .getContext()
//	            .getAuthentication();
//
//	    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
//	    
//	    // =====================================================
//	    // 1. GET ALL USER ROLES
//	    // =====================================================
//	    
//	    Set<Integer> userRoleIds = new HashSet<>();
//
//	    for (GrantedAuthority authority : user.getAuthorities()) {
//
//	        String roleName = authority.getAuthority();
//
//	        ERole role = ERole.valueOf(roleName);
//
//	        Role dbRole = roleRepo.findByName(role)
//	                .orElseThrow(() ->
//	                        new RuntimeException(
//	                                "Role not found: " + role));
//
//	        userRoleIds.add(dbRole.getId());
//	    }
//	    
//	    if (!userRoleIds.contains(current.getRoleId())) {
//	        throw new RuntimeException("Unauthorized action");
//	    }
//	    
//	    // Currently commented but later it can be invoked in place of aboe method.
////	    Set<Integer> userRoleIds = user.getAuthorities()
////	            .stream()
////	            .map(a -> a.getAuthority())
////	            .map(roleName -> ERole.valueOf(roleName))
////	            .map(role -> roleRepo.findByName(role)
////	                    .map(Role::getId)
////	                    .orElseThrow(() ->
////	                            new RuntimeException("Role not found: " + role)))
////	            .collect(Collectors.toSet());
//	    
//
//	    // =====================================================
//	    // 1. GET USER ROLE
//	    // =====================================================
//
//	    String role = user.getAuthorities()
//	            .stream()
//	            .findFirst()
//	            .map(a -> a.getAuthority())
//	            .orElseThrow(() -> new RuntimeException("No role found"));
//
//	    Integer userRoleId = roleRepo.findByName(ERole.valueOf(role))
//	            .map(Role::getId)
//	            .orElseThrow(() -> new RuntimeException("Role not found"));
//
//	    // =====================================================
//	    // 2. GET REPORT
//	    // =====================================================
//
//	    Report report = reportRepository
//	            .findByProgressReportId(reportId)
//	            .orElseThrow(() -> new RuntimeException("Report not found"));
//
//	    // =====================================================
//	    // 3. GET WORKFLOW POLICIES
//	    // =====================================================
//
//	    List<ProgressReviewPolicy> policies =
//	            policyRepo.findByProgramIdOrderBySequenceNo(
//	                    report.getProgramId());
//
//	    // =====================================================
//	    // 4. FIND CURRENT WORKFLOW STEP
//	    // =====================================================
//
//	    ProgressReviewPolicy current = policies.stream()
//	            .filter(p ->
//	                    p.getSequenceNo()
//	                            .equals(report.getCurrentSequenceNo()))
//	            .findFirst()
//	            .orElseThrow(() ->
//	                    new RuntimeException("Invalid workflow state"));
//
//	    // =====================================================
//	    // 5. CHECK WHETHER CURRENT ROLE IS AUTHORIZED
//	    // =====================================================
//
//	    if (!current.getRoleId().equals(userRoleId)) {
//	        throw new RuntimeException("Unauthorized action");
//	    }
//
//	    // =====================================================
//	    // 6. REJECT
//	    // =====================================================
//
//	    if ("REJECTED".equalsIgnoreCase(action)) {
//
//	        report.setStatus(ProgressStatus.REJECTED);
//	        report.setCurrentSequenceNo(null);
//
//	        ProgressReport pr = progressReportRepo
//	                .findById(report.getProgressReportId())
//	                .orElseThrow();
//
//	        pr.setProgressStatus(ProgressStatus.REJECTED);
//
//	        ScholarSemester ss = pr.getScholarSemester();
//	        ss.setReviewStatus(
//	                ScholarSemester.ReviewStatus.Rejected);
//
//	        progressReportRepo.save(pr);
//	        scholarSemesterRepo.save(ss);
//	    }
//
//	    // =====================================================
//	    // 7. APPROVE
//	    // =====================================================
//
//	    else if ("APPROVED".equalsIgnoreCase(action)) {
//
//	        ProgressReviewPolicy next =
//	                findNextApplicablePolicy(
//	                        policies,
//	                        current,
//	                        report);
//
//	        if (next != null) {
//
//	            // ---------------------------------------------
//	            // MOVE TO NEXT APPROVER
//	            // ---------------------------------------------
//
//	            report.setCurrentSequenceNo(
//	                    next.getSequenceNo());
//
//	            report.setStatus(
//	                    ProgressStatus.UNDERREVIEW);
//
//	        } else {
//
//	            // ---------------------------------------------
//	            // FINAL APPROVAL
//	            // PG DEAN
//	            // ---------------------------------------------
//
//	            report.setStatus(
//	                    ProgressStatus.APPROVED);
//
//	            report.setCurrentSequenceNo(null);
//
//	            ProgressReport pr = progressReportRepo
//	                    .findById(report.getProgressReportId())
//	                    .orElseThrow();
//
//	            pr.setProgressStatus(
//	                    ProgressStatus.APPROVED);
//
//	            ScholarSemester ss = pr.getScholarSemester();
//
//	            ss.setReviewStatus(
//	                    ScholarSemester.ReviewStatus.Approved);
//
//	            progressReportRepo.save(pr);
//	            scholarSemesterRepo.save(ss);
//	        }
//	    }
//
//	    // =====================================================
//	    // 8. REVISION REQUIRED
//	    // =====================================================
//
//	    else if ("REVISION_REQUIRED".equalsIgnoreCase(action)) {
//
//	        ProgressReport pr = progressReportRepo
//	                .findById(report.getProgressReportId())
//	                .orElseThrow();
//
//	        pr.setProgressStatus(
//	                ProgressStatus.REVISION_REQUIRED);
//
//	        pr.setNextActions(Remarks);
//
//	        progressReportRepo.save(pr);
//	    }
//
//	    else {
//
//	        throw new RuntimeException("Invalid action");
//	    }
//
//	    // =====================================================
//	    // 9. SAVE HISTORY
//	    // =====================================================
//
//	    ReviewHistory h = new ReviewHistory();
//
//	    h.setReportId(report.getId());
//	    h.setRoleId(userRoleId);
//	    h.setAction(action);
//	    h.setActedBy(user.getId().intValue());
//	    h.setActedAt(LocalDateTime.now());
//	    h.setRemarks(Remarks);
//
//	    historyRepo.save(h);
//
//	    // =====================================================
//	    // 10. SAVE REPORT
//	    // =====================================================
//
//	    reportRepository.save(report);
//	}
//
	
	
	public void processReview(Integer reportId, String Remarks, String action) {

	    Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();

	    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

	    // =====================================================
	    // 1. GET ALL USER ROLES
	    // =====================================================

	    Set<Integer> userRoleIds = new HashSet<>();

	    for (GrantedAuthority authority : user.getAuthorities()) {

	        String roleName = authority.getAuthority();

	        ERole role = ERole.valueOf(roleName);

	        Role dbRole = roleRepo.findByName(role)
	                .orElseThrow(() ->
	                        new RuntimeException(
	                                "Role not found: " + role));

	        userRoleIds.add(dbRole.getId());
	    }

	    // =====================================================
	    // 2. GET REPORT
	    // =====================================================

	    Report report = reportRepository
	            .findByProgressReportId(reportId)
	            .orElseThrow(() ->
	                    new RuntimeException("Report not found"));

	    // =====================================================
	    // 3. GET WORKFLOW POLICIES
	    // =====================================================

	    List<ProgressReviewPolicy> policies =
	            policyRepo.findByProgramIdOrderBySequenceNo(
	                    report.getProgramId());

	    // =====================================================
	    // 4. FIND CURRENT WORKFLOW STEP
	    // =====================================================

	    ProgressReviewPolicy current = policies.stream()
	            .filter(p ->
	                    p.getSequenceNo()
	                            .equals(report.getCurrentSequenceNo()))
	            .findFirst()
	            .orElseThrow(() ->
	                    new RuntimeException("Invalid workflow state"));

	    // =====================================================
	    // 5. CHECK WHETHER USER HAS CURRENT ROLE
	    // =====================================================

	    if (!userRoleIds.contains(current.getRoleId())) {

	        throw new RuntimeException(
	                "Unauthorized action");
	    }

	    // =====================================================
	    // IMPORTANT:
	    // Role used for history is the CURRENT WORKFLOW ROLE,
	    // not the first role of the user.
	    // =====================================================

	    Integer actingRoleId  = current.getRoleId();

	    // =====================================================
	    // 6. HANDLE REJECT
	    // =====================================================

	    if ("REJECTED".equalsIgnoreCase(action)) {

	        report.setStatus(ProgressStatus.REJECTED);
	        report.setCurrentSequenceNo(null);

	        ProgressReport pr = progressReportRepo
	                .findById(report.getProgressReportId())
	                .orElseThrow();

	        pr.setProgressStatus(
	                ProgressStatus.REJECTED);

	        ScholarSemester ss = pr.getScholarSemester();

	        ss.setReviewStatus(
	                ScholarSemester.ReviewStatus.Rejected);

	        progressReportRepo.save(pr);
	        scholarSemesterRepo.save(ss);
	    }

	    // =====================================================
	    // 7. HANDLE APPROVE
	    // =====================================================

	    else if ("APPROVED".equalsIgnoreCase(action)) {

	        ProgressReviewPolicy next =
	                findNextApplicablePolicy(
	                        policies,
	                        current,
	                        report);

	        if (next != null) {

	            // ---------------------------------------------
	            // MOVE TO NEXT APPROVER
	            // ---------------------------------------------

	            report.setCurrentSequenceNo(
	                    next.getSequenceNo());

	            report.setStatus(
	                    ProgressStatus.UNDERREVIEW);

	        } else {

	            // ---------------------------------------------
	            // FINAL APPROVAL
	            // PG DEAN
	            // ---------------------------------------------

	            report.setStatus(
	                    ProgressStatus.APPROVED);

	            report.setCurrentSequenceNo(null);

	            ProgressReport pr = progressReportRepo
	                    .findById(report.getProgressReportId())
	                    .orElseThrow();

	            pr.setProgressStatus(
	                    ProgressStatus.APPROVED);

	            ScholarSemester ss =
	                    pr.getScholarSemester();

	            ss.setReviewStatus(
	                    ScholarSemester.ReviewStatus.Approved);

	            progressReportRepo.save(pr);
	            scholarSemesterRepo.save(ss);
	        }
	    }

	    // =====================================================
	    // 8. REVISION REQUIRED
	    // =====================================================

	    else if ("REVISION_REQUIRED".equalsIgnoreCase(action)) {

	        ProgressReport pr = progressReportRepo
	                .findById(report.getProgressReportId())
	                .orElseThrow();

	        pr.setProgressStatus(
	                ProgressStatus.REVISION_REQUIRED);

	        pr.setNextActions(Remarks);

	        progressReportRepo.save(pr);
	    }

	    // =====================================================
	    // 9. INVALID ACTION
	    // =====================================================

	    else {

	        throw new RuntimeException(
	                "Invalid action");
	    }

	    // =====================================================
	    // 10. SAVE REVIEW HISTORY
	    // =====================================================

	    ReviewHistory h = new ReviewHistory();

	    h.setReportId(report.getId());

	    // IMPORTANT:
	    // Save the role under which the user acted,
	    // not an arbitrary role from the user's authorities.
	    h.setRoleId(actingRoleId);

	    h.setAction(action);
	    h.setActedBy(user.getId().intValue());
	    h.setActedAt(LocalDateTime.now());
	    h.setRemarks(Remarks);

	    historyRepo.save(h);

	    // =====================================================
	    // 11. SAVE REPORT
	    // =====================================================

	    reportRepository.save(report);
	}



	private ProgressReviewPolicy findNextApplicablePolicy(
	        List<ProgressReviewPolicy> policies,
	        ProgressReviewPolicy current,
	        Report report) {

	    List<ProgressReviewPolicy> nextPolicies =
	            policies.stream()
	                    .filter(p ->
	                            p.getSequenceNo() >
	                            current.getSequenceNo())
	                    .sorted(
	                        Comparator.comparing(
	                            ProgressReviewPolicy::getSequenceNo))
	                    .collect(Collectors.toList());

	    for (ProgressReviewPolicy policy : nextPolicies) {

	        if (isPolicyApplicable(policy, report)) {
	            return policy;
	        }
	    }

	    return null;
	}

	

	private boolean isPolicyApplicable(
	        ProgressReviewPolicy policy,
	        Report report) {

	    Integer roleId = policy.getRoleId();

	    // -------------------------------------------------
	    // SUPERVISOR
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_SUPERVISOR)) {
	        return true;
	    }

	    // -------------------------------------------------
	    // CO-SUPERVISOR
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_CO_SUPERVISOR)) {

	        return hasCoSupervisor(report);
	    }

	    // -------------------------------------------------
	    // REVIEWER
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_REVIEWER)) {

	        return isReviewerRequired(report);
	    }

	    // -------------------------------------------------
	    // HOD
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_HOD)) {
	        return true;
	    }

	    // -------------------------------------------------
	    // DEAN
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_DEAN)) {
	        return true;
	    }

	    // -------------------------------------------------
	    // PG DEAN
	    // -------------------------------------------------

	    if (isRole(roleId, ERole.ROLE_PG_DEAN)) {
	        return true;
	    }

	    // -------------------------------------------------
	    // DEFAULT
	    // -------------------------------------------------

	    return Boolean.TRUE.equals(policy.getIsMandatory());
	}

	
	private boolean isReviewerRequired(Report report) {

	    Program program = programRepo
	            .findById(report.getProgramId())
	            .orElseThrow(() ->
	                    new RuntimeException("Program not found"));

	    return Boolean.TRUE.equals(
	            program.getReviewerRequired());
	}
	
	
	private boolean hasCoSupervisor(Report report) {

	    ProgressReport pr = progressReportRepo
	            .findById(report.getProgressReportId())
	            .orElseThrow(() ->
	                    new RuntimeException("Progress report not found"));

	    ScholarSemester ss = pr.getScholarSemester();

	    Integer scholarId = ss.getScholarId();

	    return scholarSupervisorRepo
	            .existsByScholar_ScholarIdAndRoleAndIsActiveTrue(
	                    scholarId,
	                    SupervisorRole.CO_SUPERVISOR);
	}
	
	
	private boolean isRole(Integer roleId, ERole role) {

	    Integer requiredRoleId = roleRepo.findByName(role)
	            .map(Role::getId)
	            .orElse(null);

	    return requiredRoleId != null
	            && requiredRoleId.equals(roleId);
	}

	
	
	
	 
	 
}
