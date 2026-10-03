package edu.dei.examination.phd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.phd.model.Achievement;
import edu.dei.examination.phd.repository.AchievementRepository;

@Service
@Transactional
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementService(
            AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Transactional(readOnly = true)
    public List<Achievement> getAchievements(Integer reportId) {
        return achievementRepository.findByReportId(reportId);
    }

    public Achievement saveAchievement(
            Integer reportId, Achievement achievement) {

        // The report ID comes from the URL, not the request body.
        achievement.setId(null);
        achievement.setReportId(reportId);

        return achievementRepository.save(achievement);
    }

    public Optional<Achievement> updateAchievement(
            Integer id, Integer reportId, Achievement input) {

        return achievementRepository
                .findByIdAndReportId(id, reportId)
                .map(existing -> {
                    existing.setAwards(input.getAwards());
                    existing.setPatents(input.getPatents());
                    existing.setTeachingHours(input.getTeachingHours());
                    existing.setTeachingType(input.getTeachingType());

                    return achievementRepository.save(existing);
                });
    }

    public boolean deleteAchievement(Integer id, Integer reportId) {

        Optional<Achievement> achievement =
                achievementRepository.findByIdAndReportId(id, reportId);

        if (!achievement.isPresent()) {
            return false;
        }

        achievementRepository.delete(achievement.get());
        return true;
    }
}
