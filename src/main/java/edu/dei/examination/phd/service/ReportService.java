package edu.dei.examination.phd.service;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.enums.ProgressStatus;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ProgressReviewPolicy;
import edu.dei.examination.phd.model.Report;

import edu.dei.examination.phd.model.ReviewHistory;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.ProgressReviewPolicyRepository;
import edu.dei.examination.phd.repository.ReportRepository;
import edu.dei.examination.phd.repository.ReviewHistoryRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.AccessOptions.SetOptions.Propagation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
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

	 public void processReview(Integer reportId, String Remarks, String action) {

		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

		    //userRoleId=user.getAuthorities().forEach(null);
		    
		    String role = user.getAuthorities()
		            .stream()
		            .findFirst()
		            .map(a -> a.getAuthority())
		            .orElseThrow(() -> new RuntimeException("No role found"));
		    
		    Integer userRoleId = roleRepo.findByName(ERole.valueOf(role))
		            .map(Role::getId)
		            .orElseThrow(() -> new RuntimeException("Role not found"));
		    
//		    Report report = reportRepository.findById(reportId)
//		            .orElseThrow(() -> new RuntimeException("Report not found"));
		    Report report = reportRepository.findByProgressReportId(reportId)
		            .orElseThrow(() -> new RuntimeException("Report not found"));
		    

		    List<ProgressReviewPolicy> policies =
		            policyRepo.findByProgramIdOrderBySequenceNo(report.getProgramId());

		    ProgressReviewPolicy current = policies.stream()
		            .filter(p -> p.getSequenceNo().equals(report.getCurrentSequenceNo()))
		            .findFirst()
		            .orElseThrow(() -> new RuntimeException("Invalid workflow state"));

		    // ✅ Role validation
		    if (!current.getRoleId().equals(userRoleId)) {
		        throw new RuntimeException("Unauthorized action");
		    }

		    // =========================
		    // 🔴 HANDLE REJECT
		    // =========================
		    if ("REJECTED".equalsIgnoreCase(action)) {

		        report.setStatus(ProgressStatus.REJECTED);
		        report.setCurrentSequenceNo(null);

		        // ✅ Update ProgressReport
		        ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
		        pr.setProgressStatus(ProgressStatus.REJECTED); 
		       // (ProgressStatus.REJECTED);

		        // ✅ Update ScholarSemester
		        ScholarSemester ss = pr.getScholarSemester();
		        ss.setReviewStatus(ScholarSemester.ReviewStatus.Rejected);

		        progressReportRepo.save(pr);
		        scholarSemesterRepo.save(ss);
		    }

		    // =========================
		    // ✅ HANDLE APPROVE
		    // =========================
		    else if ("APPROVED".equalsIgnoreCase(action)) {

		        // 🔄 Find next step
		        ProgressReviewPolicy next = policies.stream()
		                .filter(p -> p.getSequenceNo() > current.getSequenceNo())
		                .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
		                .findFirst()
		                .orElse(null);

		        // 🔁 Skip optional
		        while (next != null && Boolean.FALSE.equals(next.getIsMandatory())) {
		            Integer nextSeq = next.getSequenceNo();

		            next = policies.stream()
		                    .filter(p -> p.getSequenceNo() > nextSeq)
		                    .sorted(Comparator.comparing(ProgressReviewPolicy::getSequenceNo))
		                    .findFirst()
		                    .orElse(null);
		        }

		        if (next != null) {
		            // 👉 Move to next reviewer
		            report.setCurrentSequenceNo(next.getSequenceNo());
		            report.setStatus(ProgressStatus.UNDERREVIEW);
		        } else {
		            // 🎯 FINAL APPROVAL
		            report.setStatus(ProgressStatus.APPROVED);
		            report.setCurrentSequenceNo(null);

		            // ✅ Update ProgressReport
		            ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
		            pr.setProgressStatus(ProgressStatus.APPROVED);

		            // ✅ Update ScholarSemester
		            ScholarSemester ss = pr.getScholarSemester();
		            ss.setReviewStatus(ScholarSemester.ReviewStatus.Approved);

		            progressReportRepo.save(pr);
		            scholarSemesterRepo.save(ss);
		        }
		    }
		    else if("REVISION_REQUIRED".equalsIgnoreCase(action)) {
		    	 ProgressReport pr = progressReportRepo.findById(report.getProgressReportId()).orElseThrow();
		            pr.setProgressStatus(ProgressStatus.REVISION_REQUIRED);
		    
              pr.setNextActions(Remarks);
              progressReportRepo.save(pr);
              
		    	
		    }

		    else {
		        throw new RuntimeException("Invalid action");
		    }

		    // =========================
		    // 📝 Save History
		    // =========================
		    ReviewHistory h = new ReviewHistory();
		    h.setReportId(report.getId());
		    h.setRoleId(userRoleId);
		    h.setAction(action);   // ✅ FIXED
		    h.setActedBy(user.getId().intValue());
		    h.setActedAt(LocalDateTime.now());
		    h.setRemarks(Remarks);

		    historyRepo.save(h);

		    // =========================
		    // 💾 Save Report
		    // =========================
		    reportRepository.save(report);
		}
}
