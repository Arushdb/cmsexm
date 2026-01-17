package edu.dei.examination.phd.controller;


import edu.dei.examination.phd.dto.ProgressReportRequest;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.service.ProgressReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

//imports omitted for brevity
@RestController
@RequestMapping("/api/progress-reports")
@CrossOrigin(origins = "*")
public class ProgressReportController {

 private final ProgressReportService svc;
 public ProgressReportController(ProgressReportService svc) { this.svc = svc; }

 @GetMapping
 public ResponseEntity<List<ProgressReport>> getByScholar(@RequestParam Integer scholarId) {
     return ResponseEntity.ok(svc.findByScholar(scholarId));
 }

 @PostMapping
 public ResponseEntity<?> create(@Valid @RequestBody ProgressReportRequest req, BindingResult br) {
     if (br.hasErrors()) {
         return ResponseEntity.badRequest().body(br.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
     }
     try {
         // map DTO -> entity
         ProgressReport pr = new ProgressReport();
         // scholar will be set in service
         pr.setSemesterId(req.getSemesterId());
         pr.setPeriodStart(req.getPeriodStart());
         pr.setPeriodEnd(req.getPeriodEnd());
         pr.setSummary(req.getSummary());
         pr.setCommitteeState(req.getCommitteeState());
         pr.setMeetingDate(req.getMeetingDate());
         pr.setNextActions(req.getNextActions());

         ProgressReport created = svc.createReport(req.getScholarId(), pr);
         return ResponseEntity.ok(created);
     } catch (IllegalArgumentException ex) {
         return ResponseEntity.badRequest().body(ex.getMessage());
     }
 }
 // other endpoints...
}
