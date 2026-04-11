package edu.dei.examination.phd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.phd.dto.ReviewerDashboardDTO;
import edu.dei.examination.phd.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByCurrentSequenceNoAndProgramId(Integer seq, Integer programId);
    
    List<Report> findByCurrentSequenceNoAndDepartmentId(Integer seq, Integer deptId);

    List<Report> findByCurrentSequenceNoAndFacultyId(Integer seq, Integer facultyId);
    
    Optional<Report> findByProgressReportId(Integer progressrportId);
  
    @Query(
    		"SELECT new edu.dei.examination.phd.dto.ReviewerDashboardDTO(" +
    		" sc.scholarId, sc.enrolmentno, sem.academicYear, ssm.id, sem.semesterName," +
    		" sc.fullName, p.programName, r.progressReportId, r.status, r.submittedOn," +
    		" ssm.totalsessions, ssm.attendedsessions, ssm.attendancePercentage, ssm.attendanceremarks) " +

    		"FROM Report r " +
    		"JOIN r.scholarSemester ssm " +
    		"JOIN ssm.scholar sc " +
    		"JOIN sc.department d " +
    		"JOIN d.roleAssignments dra " +
    		"JOIN sc.program p " +
    		"JOIN ssm.semester sem " +

    		"WHERE dra.userId = :userId " +
    		"AND dra.role = 'ROLE_SUPERVISOR' " +
    		"AND dra.isActive = true " +
    		"AND r.currentSequenceNo = 1"
    		)
    		List<ReviewerDashboardDTO> getSupervisorDashboard(@Param("userId") Integer userId);
    
    @Query(
    		"SELECT new edu.dei.examination.phd.dto.ReviewerDashboardDTO(" +
    		" sc.scholarId, sc.enrolmentno, sem.academicYear, ssm.id, sem.semesterName," +
    		" sc.fullName, p.programName, r.progressReportId, r.status, r.submittedOn," +
    		" ssm.totalsessions, ssm.attendedsessions, ssm.attendancePercentage, ssm.attendanceremarks) " +

    		" FROM Report r " +
    		" JOIN r.scholarSemester ssm " +
    		" JOIN ssm.scholar sc " +
    		
    		" JOIN sc.program p " +
    		" JOIN ssm.semester sem " +
    		" JOIN ProgramRoleAssignment fra ON fra.program = p" +

    		" WHERE fra.userId = :userId " +
    		" AND r.currentSequenceNo = 2"
    		)
    		List<ReviewerDashboardDTO> getReviewerDashboard(@Param("userId") Integer userId);
    
    
    @Query(
    		"SELECT new edu.dei.examination.phd.dto.ReviewerDashboardDTO(" +
    		" sc.scholarId, sc.enrolmentno, sem.academicYear, ssm.id, sem.semesterName," +
    		" sc.fullName, p.programName, r.progressReportId, r.status, r.submittedOn," +
    		" ssm.totalsessions, ssm.attendedsessions, ssm.attendancePercentage, ssm.attendanceremarks) " +

    		"FROM Report r " +
    		"JOIN r.scholarSemester ssm " +
    		"JOIN ssm.scholar sc " +
    		"JOIN sc.department d " +
    		"JOIN d.roleAssignments dra " +
    		"JOIN sc.program p " +
    		"JOIN ssm.semester sem " +

    		"WHERE dra.userId = :userId " +
    		"AND dra.role = 'ROLE_HOD' " +
    		"AND dra.isActive = true " +
    		"AND r.currentSequenceNo = 3"
    		)
    		List<ReviewerDashboardDTO> getHodDashboard(@Param("userId") Integer userId);
    
    @Query(
    		"SELECT new edu.dei.examination.phd.dto.ReviewerDashboardDTO(" +
    		" sc.scholarId, sc.enrolmentno, sem.academicYear, ssm.id, sem.semesterName," +
    		" sc.fullName, p.programName, r.progressReportId, r.status, r.submittedOn," +
    		" ssm.totalsessions, ssm.attendedsessions, ssm.attendancePercentage, ssm.attendanceremarks) " +

    		"FROM Report r " +
    		"JOIN r.scholarSemester ssm " +
    		"JOIN ssm.scholar sc " +
    		"JOIN sc.program p " +
    		"JOIN p.roleAssignments fra " +
    		"JOIN ssm.semester sem " +

    		"WHERE fra.userId = :userId " +
    		"AND fra.role = 'ROLE_DEAN' " +
    		"AND fra.isActive = true " +
    		"AND r.currentSequenceNo = 4"
    		)
    		List<ReviewerDashboardDTO> getDeanDashboard(@Param("userId") Integer userId);
}