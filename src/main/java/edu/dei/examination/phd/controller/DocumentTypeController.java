package edu.dei.examination.phd.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.DocumentTypeResponse;
import edu.dei.examination.phd.service.DocumentTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

    private final DocumentTypeService service;

    public DocumentTypeController(DocumentTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getDocumentTypes() {

        List<DocumentTypeResponse> types =
                service.getActiveDocumentTypes();

        return ResponseEntity.ok(
                ApiResponse.success("Document types fetched", types)
        );
    }
}
