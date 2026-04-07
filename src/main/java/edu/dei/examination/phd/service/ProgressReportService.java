package edu.dei.examination.phd.service;


import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ProgressReportRepository;

import edu.dei.examination.phd.repository.ScholarsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.dei.examination.phd.model.*;
import edu.dei.examination.phd.repository.*;
import edu.dei.examination.phd.dto.ProgressReportRequest;
import edu.dei.examination.phd.dto.ProgressReportResponse;
import edu.dei.examination.phd.enums.ProgressStatus;

@Service
public class ProgressReportService {

    private final ProgressReportRepository progressReportRepository;
    private final ScholarsRepository scholarRepo;
    
    private final ScholarSemesterRepository scholarSemesterRepository;
    @Autowired
    ReportService reportservice;

    public ProgressReportService(
            ProgressReportRepository progressReportRepository,
            ScholarsRepository scholarRepo,
            ReportRepository reportRepository,
            ScholarSemesterRepository scholarSemesterRepository) {
        this.progressReportRepository = progressReportRepository;
        this.scholarRepo = scholarRepo;
       
        this.scholarSemesterRepository= scholarSemesterRepository;
    }

    /* ================= SAVE DRAFT ================= */
    public void saveDraft(Integer userId, ProgressReportRequest request) {

        Scholars scholar = scholarRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Scholar not found"));
        
         ScholarSemester ssm=   scholarSemesterRepository.findByScholarScholarIdAndSemesterSemesterId(scholar.getScholarId(),
        		request.getSemesterRegistrationId())
        		 .orElseThrow(()->new RuntimeException("Scholar semester not found"));

//        ProgressReport report = progressReportRepository
//                .findByScholarIdAndSemesterRegistrationId(
//                        scholar.getScholarId(),
//                        request.getSemesterRegistrationId()
//                )
                //.orElse(new ProgressReport());
         
         ProgressReport report = progressReportRepository
     		    .findTopByScholarSemesterIdOrderByIdDesc(ssm.getId())
     		    .orElseGet(() -> {
     		        ProgressReport pr = new ProgressReport();
     		        pr.setScholarSemester(ssm);
     		        pr.setProgressStatus(ProgressStatus.DRAFT);
     		        return pr;
     		    });
        
//        ProgressReport report=   progressReportRepository.findByScholarSemesterId(ssm.getId())
//        		.orElseThrow(()->new RuntimeException("Scholar semester not found"));

        
        
        if ("SUBMITTED".equals(report.getProgressStatus().toString()) || 
        		"APPROVED".equals(report.getProgressStatus().toString())) {
            throw new RuntimeException("Progress report is locked. Changes are not allowed.");
        }

        validateEditable(report);
//        ScholarSemester ssm=  scholarSemesterRepository.findByScholarScholarIdAndSemesterSemesterId(scholar.getScholarId(),
//        		request.getSemesterRegistrationId())
//        		.orElseThrow(()->new RuntimeException("Scholar semester not found"));
//        report.setScholarId(scholar.getScholarId());
//        report.setSemesterRegistrationId(request.getSemesterRegistrationId());
        report.setScholarSemester(ssm);
        report.setLastSemesterRegistrationId(
                request.getLastSemesterRegistrationId()
        );
       
        report.setResearchWork(request.getResearchWork());
        report.setConference(request.getConference());
        report.setResearchPaper(request.getResearchPaper());
        report.setTours(request.getTours());
        
        report.setSummary(request.getSummary());
       // report.setNextActions(request.getNextActions());

        //report.setProgressStatus(ProgressStatus.DRAFT);

        progressReportRepository.save(report);
    }

    /* ================= SUBMIT ================= */
    @Transactional(rollbackFor = Exception.class,transactionManager = "phdTransactionManager")
    public void submitReport(Integer userId,String username,ProgressReportRequest request )   {
    	
    	System.out.println("TX ACTIVE: " + 
    		    org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive());

        Scholars scholar = scholarRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Scholar not found"));
        
        ScholarSemester ssm=   scholarSemesterRepository.findByScholarScholarIdAndSemesterSemesterId(scholar.getScholarId(),
        		request.getSemesterRegistrationId())
        		 .orElseThrow(()->new RuntimeException("Scholar semester not found"));
        
        ProgressReport report = progressReportRepository
     		    .findTopByScholarSemesterIdOrderByIdDesc(ssm.getId())
     		    .orElseThrow(()->new RuntimeException("No draft found to submit"));

//        ProgressReport report = progressReportRepository
//                .findByScholarIdAndSemesterRegistrationId(
//                        scholar.getScholarId(),
//                        request.getSemesterRegistrationId()
//                )
//                .orElseThrow(() ->
//                        new IllegalArgumentException("No draft found to submit"));
        
        
        

        if (report.getProgressStatus() == ProgressStatus.SUBMITTED) {
            throw new IllegalArgumentException("Report already submitted");
        }
        
        if (report.getProgressStatus() == ProgressStatus.APPROVED) {
            throw new IllegalArgumentException("Report already approved");
            
        }
        validateEditable(report);

        report.setProgressStatus(ProgressStatus.SUBMITTED);
        report.setSubmittedAt(LocalDateTime.now());
        
        

       
        
        progressReportRepository.save(report);
       

      
//        ScholarSemester ss = scholarSemesterRepository
//        		.findByScholarScholarIdAndSemesterSemesterId(report.getsd(), report.getSemesterRegistrationId())
//        		.orElseThrow(()->new RuntimeException("Scholar semester not found"));
//        		.findByScholarIdAndSemesterId(report.getScholarId(), report.getSemesterRegistrationId())
//        		.orElseThrow(()->new RuntimeException("Scholar semester not found"));
//        
       
       
        
        
        reportservice.createReport(username,report);
        
    }
    
       
    @Transactional(readOnly = true)
    public ProgressReportResponse getReportBySemester(
            Integer userId,
            Integer semesterRegistrationId) {

        Scholars scholar = scholarRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Scholar not found"));
        
        ScholarSemester ssm=   scholarSemesterRepository.findByScholarScholarIdAndSemesterSemesterId(scholar.getScholarId(),
        		semesterRegistrationId)
        		 .orElseThrow(()->new RuntimeException("Scholar semester not found"));

//        ProgressReport report = progressReportRepository
//                .findByScholarIdAndSemesterRegistrationId(
//                        scholar.getScholarId(),
//                        semesterRegistrationId)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("No progress report found"));
        
        ProgressReport report =progressReportRepository.
        		findTopByScholarSemesterIdOrderByIdDesc(ssm.getId())
        		.orElseThrow(() ->
                new IllegalArgumentException("No progress report found"));
        

        return mapToResponse(report);
    }
    
    
    
    
//    @Transactional(readOnly = true)
//    public List<ProgressReportResponse> getAllReports(Integer userId) {
//
//        Scholars scholar = scholarRepo.findByUserId(userId)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("Scholar not found"));
//
//        return progressReportRepository.findByScholarId(scholar.getScholarId())
//                .stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
    
    private ProgressReportResponse mapToResponse(ProgressReport report) {

        ProgressReportResponse dto = new ProgressReportResponse();

        dto.setId(report.getId());
        dto.setSemesterRegistrationId(report.getScholarSemester().getSemester().getSemesterId());
       
        dto.setResearchWork(report.getResearchWork());
        dto.setConference(report.getConference());
        dto.setResearchPaper(report.getResearchPaper());
        dto.setTours(report.getTours());
        dto.setPeriodStart(report.getPeriodStart());
        dto.setPeriodEnd(report.getPeriodEnd());
        dto.setSummary(report.getSummary());
        dto.setNextActions(report.getNextActions());
        dto.setProgressStatus(report.getProgressStatus());
        dto.setInsertTime(report.getInsertTime());

        return dto;
    }

    private void validateEditable(ProgressReport report) {
        if (!"DRAFT".equals(report.getProgressStatus().toString())) {
            throw new RuntimeException("Progress report cannot be modified.");
        }
    }

}
