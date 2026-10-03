package edu.dei.examination.phd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.model.Achievement;
import edu.dei.examination.phd.service.AchievementService;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "http://localhost:4200")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(
            AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // Get all achievements for a progress report
    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<Achievement>> getAchievements(
            @PathVariable Integer reportId) {

        List<Achievement> achievements =
                achievementService.getAchievements(reportId);

        return ResponseEntity.ok(achievements);
    }

    // Save a new achievement
    @PostMapping("/{reportId}")
    public ResponseEntity<Achievement> saveAchievement(
            @PathVariable Integer reportId,
            @RequestBody Achievement achievement) {

        Achievement saved =
                achievementService.saveAchievement(reportId, achievement);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Update an existing achievement belonging to this report
    @PutMapping("/{reportId}/{id}")
    public ResponseEntity<Achievement> updateAchievement(
            @PathVariable Integer reportId,
            @PathVariable Integer id,
            @RequestBody Achievement achievement) {

        Optional<Achievement> updated =
                achievementService.updateAchievement(
                        id, reportId, achievement);

        return updated
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an achievement belonging to this report
    @DeleteMapping("/{reportId}/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @PathVariable Integer reportId,
            @PathVariable Integer id) {

        boolean deleted =
                achievementService.deleteAchievement(id, reportId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}