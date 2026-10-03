package edu.dei.examination.phd.controller;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.model.Visit;
import edu.dei.examination.phd.service.VisitService;


@RestController
@RequestMapping("/api/visits")
@CrossOrigin
public class VisitController {

    @Autowired
    private VisitService visitService;


    // =========================================
    // SAVE VISIT
    // =========================================

    @PostMapping("/{reportId}")
    public ResponseEntity<Visit> saveVisit(
            @PathVariable Integer reportId,
            @RequestBody Visit visit) {

        Visit savedVisit =
                visitService.saveVisit(reportId, visit);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedVisit);

        
    }


    // ============================================
    // GET ALL VISITS FOR REPORT
    // ============================================

    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<Visit>> getByReportId(
            @PathVariable Integer reportId) {

        return ResponseEntity.ok(
                visitService.getVisits(reportId)
        );
    }


    // ============================================
    // GET ONE VISIT
    // Only if visit belongs to reportId
    // ============================================

    @GetMapping("/{reportId}/{id}")
    public ResponseEntity<Visit> getById(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        Optional<Visit> visit =
                visitService.getByIdAndReportId(
                        id,
                        reportId
                );

        if (visit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(visit.get());
    }


    // ============================================
    // UPDATE VISIT
    // Only if visit belongs to reportId
    // ============================================

    
    @PutMapping("/{reportId}/{id}")
    public ResponseEntity<Visit> update(
            @PathVariable Integer reportId,
            @PathVariable Integer id,
            @RequestBody Visit visit) {

        Optional<Visit> updated =
                visitService.update(
                        id,
                        reportId,
                        visit
                );

        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated.get());
    }


    // ============================================
    // DELETE VISIT
    // Only if visit belongs to reportId
    // ============================================

    @DeleteMapping("/{reportId}/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        boolean deleted =
                visitService.deleteVisit(
                        id,
                        reportId
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
