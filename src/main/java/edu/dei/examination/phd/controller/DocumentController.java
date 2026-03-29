package edu.dei.examination.phd.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.dei.examination.cmsexm.service.UserDetailsImpl;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.DocumentRequest;
import edu.dei.examination.phd.dto.DocumentResponse;
import edu.dei.examination.phd.dto.DocumentTypeResponse;
import edu.dei.examination.phd.dto.ProgressReportRequest;
import edu.dei.examination.phd.model.Document;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.DocumentRepository;
import edu.dei.examination.phd.service.DocumentService;
import edu.dei.examination.phd.service.DocumentTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService docservice;
    private final DocumentTypeService dtservice;
    private final DocumentRepository documentRepository;

    public DocumentController(DocumentService service,
    		DocumentTypeService dtservice,DocumentRepository documentRepository) {
        this.docservice = service;
        this.dtservice=dtservice;
        this.documentRepository=documentRepository;
    }

    @PostMapping("{module}/{reportId}/upload")
    public ResponseEntity<ApiResponse<?>> upload(
    		@RequestParam("file") MultipartFile file,
    		@PathVariable String reportId,
    		@PathVariable String module,
    		@ModelAttribute @RequestBody DocumentRequest request
    		 ) throws Exception {
           
//            @RequestParam Integer relatedId,
//            @RequestParam Integer documentTypeId
   

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl user =
                (UserDetailsImpl) auth.getPrincipal();
       

        DocumentResponse response =
        		docservice.uploadDocument(
                        user.getId().intValue(),
                        file,
                        reportId,
                        module,
                          // controlled internally
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success("Uploaded successfully", response)
        );
    }

    @GetMapping("/progress/{relatedId}")
    public ResponseEntity<ApiResponse<?>> getDocuments(
            @PathVariable Integer relatedId, @RequestParam(required = false) Integer scholarId) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl user =
                (UserDetailsImpl) auth.getPrincipal();
        
        
        

        List<DocumentResponse> docs =
        		docservice.getDocuments(
                        user.getId().intValue(),
                        "progress_report",
                        relatedId,  scholarId
                        
                );

        return ResponseEntity.ok(
                ApiResponse.success("Fetched successfully", docs)
        );
    }
    @GetMapping("/document-types")
    public ResponseEntity<ApiResponse<?>> getDocumentTypes() {

        List<DocumentTypeResponse> types =
        		dtservice.getActiveDocumentTypes();

        return ResponseEntity.ok(
                ApiResponse.success("Document types fetched", types)
        );
    }
    
    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer documentId) {

        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return docservice.downloadDocument(doc);
    }
}
