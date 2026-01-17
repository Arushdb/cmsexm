package edu.dei.examination.phd.controller;


import edu.dei.examination.phd.dto.RemarkRequest;
import edu.dei.examination.phd.model.ReviewerRemark;
import edu.dei.examination.phd.service.RemarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/remarks")
@CrossOrigin(origins = "*")
public class RemarkController {

    private final RemarkService svc;

    public RemarkController(RemarkService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<ReviewerRemark>> list(@RequestParam String context, @RequestParam Integer contextId) {
        return ResponseEntity.ok(svc.findRemarks(context, contextId));
    }

    // only supervisors/co-supervisors/HOD/DEAN allowed to add remarks
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR','CO_SUPERVISOR','HOD','DEAN','ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody RemarkRequest req, BindingResult br, Principal principal) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
        }
        // map DTO -> entity
        ReviewerRemark r = new ReviewerRemark();
        r.setReviewContext(req.getReviewContext());
        r.setContextId(req.getContextId());
        r.setRemarkText(req.getRemarkText());
        r.setIsPrivate(Boolean.TRUE.equals(req.getIsPrivate()));
        // set reviewerRole from principal roles (optional override)
        r.setReviewerRole(req.getReviewerRole());
        // optionally set reviewerId if principal maps to a supervisor record
        // r.setReviewerId(...);

        ReviewerRemark created = svc.addRemark(r);
        return ResponseEntity.ok(created);
    }
}

