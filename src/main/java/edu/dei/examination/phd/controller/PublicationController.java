package edu.dei.examination.phd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.model.Achievement;
import edu.dei.examination.phd.model.Publication;
import edu.dei.examination.phd.service.PublicationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publications")

@CrossOrigin
public class PublicationController {

	@Autowired
    private  PublicationService publicationService;
    
    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<Publication> savePublication(
            @PathVariable Integer reportId,
            @RequestBody Publication publication) {
    	
    	Publication saved =publicationService.savePublication(reportId, publication);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        
    }

    @GetMapping("/{reportId}")
    public List<Publication> getPublications(
            @PathVariable Integer reportId) {

        return publicationService.getPublicationsByReportId(reportId);
    }

    
    // Update an existing achievement belonging to this report
    @PutMapping("/{reportId}/{id}")
    public ResponseEntity<Publication> updatePublication(
            @PathVariable Integer reportId,
            @PathVariable Integer id,
            @RequestBody Publication publication) {

        Optional<Publication> updated =
        		publicationService.updatePublication(
                        id, reportId, publication);

        return updated
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    
    @DeleteMapping("/{reportId}/{id}")
    public ResponseEntity<Void> deletePublication(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

    	 boolean deleted =publicationService.deletePublication(id, reportId);
        
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}