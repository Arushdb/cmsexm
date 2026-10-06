package edu.dei.examination.phd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.phd.dto.ProgressWorkDto;
import edu.dei.examination.phd.model.ProgressWork;

import edu.dei.examination.phd.service.ProgressWorkService;

@RestController
@RequestMapping("/api/progress-work")

public class ProgressWorkController {

    @Autowired
    private ProgressWorkService service;

    @PostMapping("/{reportId}")
    public ResponseEntity<ProgressWork> save(
            @PathVariable Integer reportId,
            @RequestBody ProgressWork work) {

        return ResponseEntity.ok(
                service.saveProgressWork(reportId, work));
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<ProgressWork>> getByReportId(
            @PathVariable Integer reportId) {

        return ResponseEntity.ok(
                service.getProgressWork(reportId));
    }

    @PutMapping("/{reportId}/{id}")
    public ResponseEntity<ProgressWork> update(
            @PathVariable Integer reportId,
            @PathVariable Integer id,
            @RequestBody ProgressWork work) {

        return service.updateProgressWork(id, reportId, work)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reportId}/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        boolean deleted = service.deleteProgressWork(id, reportId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}