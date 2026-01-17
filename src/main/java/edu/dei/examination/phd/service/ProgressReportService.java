package edu.dei.examination.phd.service;


import edu.dei.examination.phd.model.ProgressReport;

import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ProgressReportRepository;

import edu.dei.examination.phd.repository.ScholarsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProgressReportService {

    private final ProgressReportRepository reportRepo;
    private final ScholarsRepository scholarRepo;

    public ProgressReportService(ProgressReportRepository reportRepo, ScholarsRepository scholarRepo) {
        this.reportRepo = reportRepo;
        this.scholarRepo = scholarRepo;
    }

    public List<ProgressReport> findByScholar(Integer scholarId) {
        return reportRepo.findByScholar_ScholarIdOrderByCreatedAtDesc(scholarId);
    }

    @Transactional
    public ProgressReport createReport(Integer scholarId, ProgressReport payload) {
        Optional<Scholars> s = scholarRepo.findById(scholarId);
        if (!s.isPresent()) throw new IllegalArgumentException("Scholar not found: " + scholarId);
        //payload.setScholar(s.get());
        return reportRepo.save(payload);
    }

    public Optional<ProgressReport> findById(Integer reportId) {
        return reportRepo.findById(reportId);
    }
}
