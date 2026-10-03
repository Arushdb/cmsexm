package edu.dei.examination.phd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.Visit;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.VisitRepository;


@Service
@Transactional
public class VisitService {

    private final VisitRepository visitRepository;
    

    public VisitService(
            VisitRepository visitRepository,
            ProgressReportRepository progressReportRepository) {

        this.visitRepository = visitRepository;
        
    }


    // =========================
    // GET VISITS
    // =========================

    @Transactional(readOnly = true)
    public List<Visit> getVisits(Integer reportId) {

        return visitRepository
                .findByReportId(reportId);
    }

 // ============================================
    // GET ONE VISIT
    // Only if it belongs to reportId
    // ============================================

    public Optional<Visit> getByIdAndReportId(
            Integer id,
            Integer reportId) {

        return visitRepository.findByIdAndReportId(
                id,
                reportId
        );
    }

    // ============================================
    // SAVE NEW VISIT
    // ============================================

    public Visit save(Visit visit) {

        return visitRepository.save(visit);
    }
       public Visit saveVisit(
            Integer reportId,
            Visit visit) {

        visit.setReportId(reportId);

        return visitRepository.save(visit);
    }

    // ============================================
       // UPDATE VISIT
       // Only if it belongs to reportId
       // ============================================

       public Optional<Visit> update(
               Integer id,
               Integer reportId,
               Visit updated) {

           Optional<Visit> existing =
        		   visitRepository.findByIdAndReportId(
                           id,
                           reportId
                   );

           if (existing.isEmpty()) {
               return Optional.empty();
           }

           Visit visit = existing.get();

           visit.setInstitute(
                   updated.getInstitute()
           );

           visit.setContactPerson(
                   updated.getContactPerson()
           );

           visit.setDesignation(
                   updated.getDesignation()
           );

           visit.setPlace(
                   updated.getPlace()
           );

           visit.setDates(
                   updated.getDates()
           );

           visit.setYear(
                   updated.getYear()
           );

           visit.setPurpose(
                   updated.getPurpose()
           );

           return Optional.of(
        		   visitRepository.save(visit)
           );
       }


       // ============================================
       // DELETE VISIT
       // Only if it belongs to reportId
       // ============================================

       public boolean deleteVisit(
               Integer id,
               Integer reportId) {

           Optional<Visit> visit =
        		   visitRepository.findByIdAndReportId(
                           id,
                           reportId
                   );

           if (visit.isEmpty()) {
               return false;
           }

           visitRepository.delete(visit.get());

           return true;
       }

    
}