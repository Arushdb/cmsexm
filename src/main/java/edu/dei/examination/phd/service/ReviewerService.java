package edu.dei.examination.phd.service;

import edu.dei.examination.phd.dto.RemarkRequest;
import edu.dei.examination.phd.dto.ReviewDetailDTO;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.enums.ProgressStatus;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ReviewerRemark;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSemester.ReviewStatus;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.ReportRepository;
import edu.dei.examination.phd.repository.ReviewerRemarkRepository;
import edu.dei.examination.phd.repository.ReviewerRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SupervisorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.management.RuntimeErrorException;

@Service
public class ReviewerService {

    private final ReviewerRepository reviewerRepository;
    private final ProgressReportRepository progressReportRepository;
    private final ScholarSemesterRepository scholarSemesterRepository;
    private final SupervisorRepository supervisorRepository;
    private final ScholarsRepository scholarsRepository;
    private final ReportRepository reportRepository;

    public ReviewerService(
            ReviewerRepository reviewerRepository,
            ProgressReportRepository progressReportRepository,
            ScholarSemesterRepository scholarSemesterRepository,
            SupervisorRepository supervisorRepository,
            ScholarsRepository scholarsRepository,
            ReportRepository reportRepository
            
    		) {

        this.reviewerRepository = reviewerRepository;
        this.progressReportRepository = progressReportRepository;
        this.scholarSemesterRepository = scholarSemesterRepository;
        this.supervisorRepository = supervisorRepository;
        this.scholarsRepository=scholarsRepository;
        this.reportRepository=reportRepository;
    }

    // ===============================
    // 1️⃣ Dashboard
    // ===============================
    public List<ReviewerDashboardDTO> getReviewerDashboard(Integer supervisorId) {

        return reportRepository.getReviewerDashboard(supervisorId);
    }

    // ===============================
    // 2️⃣ Get Report Details (WITH ATTENDANCE)
    // ===============================
    public ReviewDetailDTO getReportDetails(Integer reportId) {

        ProgressReport report = progressReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        
        String  sqno=report.getReport().getCurrentSequenceNo().toString();
//        ScholarSemester ss = scholarSemesterRepository
//        		.findByScholarScholarIdAndSemesterSemesterId(report.getScholarId(),
//        				report.getSemesterRegistrationId())
//        		 .orElseThrow(() -> new RuntimeException("Semester data not found"));
//        		.findByScholarIdAndSemesterId(report.getScholarId(), report.getSemesterRegistrationId())
                
        ScholarSemester ss =scholarSemesterRepository.findById(report.getScholarSemester().getId())
        		 .orElseThrow(() -> new RuntimeException("Semester data not found"));
        
        Scholars scholar = scholarsRepository
        					.findById(ss.getScholarId())
        					.orElseThrow(()->new RuntimeException("Scholar data not found"));
        					;
        					
//       List<ReviewerRemark>	theReviewerRemark =reviewerRemarkRepository.findByReviewContextAndContextIdOrderByRemarkDateAsc("PROGRESS_REPORT",
//        							reportId) ;					
        
        

        return new ReviewDetailDTO(
                report,
                ss.getAttendancePercentage(),
                ss.getAttendanceremarks(),
                ss.getOverallRemarks(),
                scholar.getFullName(),
                scholar.getEnrolmentno(),
                scholar.getProgram().getProgramName()
                
               // theReviewerRemark
        );
    }

    // ===============================
    // 3️⃣ Approve / Reject / Revision
    // ===============================
//    public void reviewReport(Integer reportId, String decision, String remarks) {
//
//        ProgressReport report = progressReportRepository.findById(reportId)
//                .orElseThrow(() -> new RuntimeException("Report not found"));
//
//        Integer scholarid = report.getScholarId();
//        Integer semesterRegistrationId = report.getSemesterRegistrationId();
//        ScholarSemester ss = scholarSemesterRepository
//        		.findByScholarScholarIdAndSemesterSemesterId(scholarid, semesterRegistrationId)
//        		.orElseThrow(() -> new RuntimeException("Semester not found"));
//                //.findByScholarIdAndSemesterId(scholarid, semesterRegistrationId)
//                
//
//        switch (decision) {
//
//            case "APPROVED":
//                report.setProgressStatus(ProgressStatus.APPROVED);
//                ss.setReviewStatus(ReviewStatus.Approved);
//                ss.setOverallRemarks(remarks);
//                break;
//
//            case "REVISION":
//                report.setProgressStatus(ProgressStatus.REVISION_REQUIRED);
//                report.setNextActions(remarks);
//                break;
//
//            case "REJECTED":
//                report.setProgressStatus(ProgressStatus.REJECTED);
//                ss.setReviewStatus(ReviewStatus.Rejected);
//                ss.setOverallRemarks(remarks);
//                break;
//        }
//
//        progressReportRepository.save(report);
//        scholarSemesterRepository.save(ss);
//    }
//
    // ===============================
    // 4️⃣ Get Supervisor from User
    // ===============================
    public Integer getSupervisorIdByUser(Integer userId) {

        return supervisorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"))
                .getSupervisorId();
    }

    // ===============================
    // 5️⃣ Mock Login (Replace later)
    // ===============================
    public Integer getLoggedInUserId() {
        return 1;
    }

	public void submitReview(RemarkRequest payload) {
		// TODO Auto-generated method stub
		Integer reportid = payload.getContextId();
		if ((reportid ==0)||(reportid ==null))
			throw (new RuntimeException("Report ID not found"));
		
		ProgressReport progressReport=  progressReportRepository.findById(reportid).orElseThrow(() -> new RuntimeException("Progress report not found"));
		
		ProgressStatus status =ProgressStatus.valueOf(payload.getDecision().toUpperCase());
		progressReport.setProgressStatus(status);
		progressReportRepository.save(progressReport)	;
		
		
	}

	public void saveAttendance(Integer scholarSemesterId, Integer scholarId, Integer totalsessions,
			Integer attendedsessions, Double attendancePercentage, String attendanceremarks) {
		// TODO Auto-generated method stub
		
//		ScholarSemester ss = scholarSemesterRepository
//				.findByScholarScholarIdAndSemesterSemesterId(scholarId, scholarSemesterId)
//				//findByScholarIdAndSemesterId(scholarId, scholarSemesterId)
//				.orElseThrow(()->new RuntimeException(" Scholar semester record not found") );
//	
		ScholarSemester ss =scholarSemesterRepository.findById(scholarSemesterId)
				.orElseThrow(()->new RuntimeException(" Scholar semester record not found") );  ;
		
		ss.setAttendancePercentage(attendancePercentage);
		ss.setTotalsessions(totalsessions);
		ss.setAttendanceremarks(attendanceremarks);
		ss.setAttendedsessions(attendedsessions); 
		scholarSemesterRepository.save(ss);
		
		
	}
}