package edu.dei.examination.phd.service;

import edu.dei.examination.phd.dto.ReviewDetailDTO;
import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.enums.ProgressStatus;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ReviewerRemark;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSemester.ReviewStatus;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.ReviewerRemarkRepository;
import edu.dei.examination.phd.repository.ReviewerRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SupervisorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewerService {

    private final ReviewerRepository reviewerRepository;
    private final ProgressReportRepository progressReportRepository;
    private final ScholarSemesterRepository scholarSemesterRepository;
    private final SupervisorRepository supervisorRepository;
    private final ScholarsRepository scholarsRepository;
    private final ReviewerRemarkRepository reviewerRemarkRepository;

    public ReviewerService(
            ReviewerRepository reviewerRepository,
            ProgressReportRepository progressReportRepository,
            ScholarSemesterRepository scholarSemesterRepository,
            SupervisorRepository supervisorRepository,
            ScholarsRepository scholarsRepository,
            ReviewerRemarkRepository reviewerRemarkRepository
            
    		) {

        this.reviewerRepository = reviewerRepository;
        this.progressReportRepository = progressReportRepository;
        this.scholarSemesterRepository = scholarSemesterRepository;
        this.supervisorRepository = supervisorRepository;
        this.scholarsRepository=scholarsRepository;
        this.reviewerRemarkRepository=reviewerRemarkRepository;
    }

    // ===============================
    // 1️⃣ Dashboard
    // ===============================
    public List<ReviewerDashboardDTO> getReviewerDashboard(Integer supervisorId) {

        return reviewerRepository.getReviewerDashboard(supervisorId);
    }

    // ===============================
    // 2️⃣ Get Report Details (WITH ATTENDANCE)
    // ===============================
    public ReviewDetailDTO getReportDetails(Integer reportId) {

        ProgressReport report = progressReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        
        
        ScholarSemester ss = scholarSemesterRepository
        		.findByScholarIdAndSemesterId(report.getScholarId(), report.getSemesterRegistrationId())
                
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
    public void reviewReport(Integer reportId, String decision, String remarks) {

        ProgressReport report = progressReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ScholarSemester ss = scholarSemesterRepository
                .findById(report.getSemesterRegistrationId())
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        switch (decision) {

            case "APPROVED":
                report.setProgressStatus(ProgressStatus.APPROVED);
                ss.setReviewStatus(ReviewStatus.Approved);
                ss.setOverallRemarks(remarks);
                break;

            case "REVISION":
                report.setProgressStatus(ProgressStatus.REVISION_REQUIRED);
                report.setNextActions(remarks);
                break;

            case "REJECTED":
                report.setProgressStatus(ProgressStatus.REJECTED);
                ss.setReviewStatus(ReviewStatus.Rejected);
                ss.setOverallRemarks(remarks);
                break;
        }

        progressReportRepository.save(report);
        scholarSemesterRepository.save(ss);
    }

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
}