package edu.dei.examination.phd.service;


import edu.dei.examination.phd.model.ResearchTopic;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ResearchTopicRepository;

import edu.dei.examination.phd.repository.ScholarsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResearchTopicService {

    private final ResearchTopicRepository topicRepo;
    private final ScholarsRepository scholarRepo;

    public ResearchTopicService(ResearchTopicRepository topicRepo, ScholarsRepository scholarRepo) {
        this.topicRepo = topicRepo;
        this.scholarRepo = scholarRepo;
    }

    public List<ResearchTopic> findByScholar(Integer scholarId) {
        return topicRepo.findByScholar_ScholarIdOrderByCreatedAtDesc(scholarId);
    }

    @Transactional
    public ResearchTopic createTopic(Integer scholarId, ResearchTopic payload) {
        Optional<Scholars> s = scholarRepo.findById(scholarId);
        if (!s.isPresent()) throw new IllegalArgumentException("Scholar not found: " + scholarId);
        //payload.setScholar(s.get());
        //payload.setScholar(
        		;
        return topicRepo.save(payload);
    }

    @Transactional
    public ResearchTopic updateTopic(Integer topicId, ResearchTopic payload) {
        ResearchTopic existing = topicRepo.findById(topicId).orElseThrow(() -> new IllegalArgumentException("Topic not found: " + topicId));
        if (payload.getTitle() != null) existing.setTitle(payload.getTitle());
        if (payload.getDescription() != null) existing.setDescription(payload.getDescription());
        if (payload.getStatus() != null) existing.setStatus(payload.getStatus());
        return topicRepo.save(existing);
    }
}
