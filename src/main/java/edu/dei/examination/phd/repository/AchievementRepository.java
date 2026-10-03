package edu.dei.examination.phd.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.Achievement;

@Repository
public interface AchievementRepository
        extends JpaRepository<Achievement, Integer> {

    List<Achievement> findByReportId(Integer reportId);

    Optional<Achievement> findByIdAndReportId(
            Integer id, Integer reportId);
}