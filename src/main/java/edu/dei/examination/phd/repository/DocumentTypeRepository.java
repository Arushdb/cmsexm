package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.dei.examination.phd.model.DocumentType;

import java.util.List;

public interface DocumentTypeRepository
        extends JpaRepository<DocumentType, Integer> {

    List<DocumentType> findByActiveTrue();
}
