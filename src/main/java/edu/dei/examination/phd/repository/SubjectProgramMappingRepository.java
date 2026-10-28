package edu.dei.examination.phd.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.phd.model.SubjectProgramMapping;


public interface SubjectProgramMappingRepository
        extends JpaRepository<SubjectProgramMapping, Long> {

    Optional<SubjectProgramMapping>
        findBySubjectKeyAndActiveTrue(String subjectKey);
}