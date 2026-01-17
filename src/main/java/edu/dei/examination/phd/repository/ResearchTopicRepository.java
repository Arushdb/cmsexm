package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.ResearchTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResearchTopicRepository extends JpaRepository<ResearchTopic, Integer> {
    List<ResearchTopic> findByScholar_ScholarIdOrderByCreatedAtDesc(Integer scholarId);
}

