package edu.dei.examination.phd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.model.ConferenceAttended;
import edu.dei.examination.phd.service.ConferenceAttendedService;

@RestController
@RequestMapping("/api/conferences")
@CrossOrigin
public class ConferenceAttendedController {

    private final ConferenceAttendedService service;

    public ConferenceAttendedController(
            ConferenceAttendedService service) {
        this.service = service;
    }


    // =====================================================
    // GET ALL CONFERENCES FOR A REPORT
    // =====================================================

    @GetMapping("/{reportId}")
    public ResponseEntity<List<ConferenceAttended>> getByReportId(
            @PathVariable Integer reportId) {

        List<ConferenceAttended> conferences =
                service.getByReportId(reportId);

        return ResponseEntity.ok(conferences);
    }


    // =====================================================
    // GET ONE CONFERENCE
    // Only if it belongs to the supplied report
    // =====================================================

    @GetMapping("/{reportId}/{id}")
    public ResponseEntity<ConferenceAttended> getById(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        Optional<ConferenceAttended> conference =
                service.getByIdAndReportId(id, reportId);

        if (conference.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conference.get());
    }


    // =====================================================
    // CREATE NEW CONFERENCE
    // =====================================================

    
    @PostMapping("/{reportId}")
    public ResponseEntity<ConferenceAttended> create(
    		@PathVariable Integer reportId,
            @RequestBody ConferenceAttended conference) {

        ConferenceAttended saved =
                service.save(reportId,conference);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }


    // =====================================================
    // UPDATE CONFERENCE
    // Only if it belongs to the supplied report
    // =====================================================

    @PutMapping("/{reportId}/{id}")
    public ResponseEntity<ConferenceAttended> update(
            @PathVariable Integer reportId,
            @PathVariable Integer id,
            @RequestBody ConferenceAttended conference) {

        Optional<ConferenceAttended> updated =
                service.update(id, reportId, conference);

        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated.get());
    }


    // =====================================================
    // DELETE CONFERENCE
    // Only if it belongs to the supplied report
    // =====================================================

    @DeleteMapping("/{reportId}/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        boolean deleted =
                service.delete(id, reportId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}