package edu.dei.examination.phd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.phd.dto.ProgressWorkDto;
import edu.dei.examination.phd.model.ProgressWork;

import edu.dei.examination.phd.service.ProgressWorkService;

@RestController
@RequestMapping("/api/progress-work")

public class ProgressWorkController {

    private final ProgressWorkService service;
    public ProgressWorkController(ProgressWorkService service) {
        this.service = service;
    }

    

    @PostMapping("/{reportId}")
    public ResponseEntity<?> saveProgressWork(
            @PathVariable Integer reportId,
            @RequestBody List<ProgressWork> rows) {

        service.saveProgressWork(reportId, rows);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reportId}")
    public List<ProgressWork> getProgressWork(
            @PathVariable Integer reportId) {

        return service.getByReportId(reportId);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgressWork(
            @PathVariable int id) {

        service.deleteProgressWork(id);

        return ResponseEntity.ok("Record deleted successfully");
    }
}