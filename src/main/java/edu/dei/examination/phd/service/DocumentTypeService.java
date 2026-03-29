package edu.dei.examination.phd.service;

import org.springframework.stereotype.Service;

import edu.dei.examination.phd.dto.DocumentTypeResponse;
import edu.dei.examination.phd.model.DocumentType;

import edu.dei.examination.phd.repository.DocumentTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeService {

    private final DocumentTypeRepository repository;

    public DocumentTypeService(DocumentTypeRepository repository) {
        this.repository = repository;
    }

    public List<DocumentTypeResponse> getActiveDocumentTypes() {

        List<DocumentType> types = repository.findByActiveTrue();

        return types.stream()
                .map(t -> new DocumentTypeResponse(
                        t.getId(),
                        t.getName()))
                .collect(Collectors.toList());
    }
}
