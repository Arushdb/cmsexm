package edu.dei.examination.phd.controller;


import edu.dei.examination.phd.model.ResearchTopic;
import edu.dei.examination.phd.service.ResearchTopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "*")
public class ResearchTopicController {

    private final ResearchTopicService svc;

    public ResearchTopicController(ResearchTopicService svc) { this.svc = svc; }

    // GET /api/topics?scholarId=#
    @GetMapping
    public ResponseEntity<List<ResearchTopic>> list(@RequestParam Integer scholarId) {
        return ResponseEntity.ok(svc.findByScholar(scholarId));
    }

    // POST /api/topics?scholarId=#
    @PostMapping
    public ResponseEntity<?> create(@RequestParam Integer scholarId, @RequestBody ResearchTopic payload) {
        try {
            ResearchTopic created = svc.createTopic(scholarId, payload);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // PUT /api/topics/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ResearchTopic payload) {
        try {
            ResearchTopic updated = svc.updateTopic(id, payload);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
