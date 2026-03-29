package edu.dei.examination.phd.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import edu.dei.examination.phd.model.Document;
import edu.dei.examination.phd.model.DocumentType;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.dto.DocumentRequest;
import edu.dei.examination.phd.dto.DocumentResponse;
import edu.dei.examination.phd.repository.DocumentRepository;
import edu.dei.examination.phd.repository.DocumentTypeRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final ScholarsRepository scholarRepo;
    private final String uploadDir = "uploads/";

    public DocumentService(DocumentRepository documentRepository,DocumentTypeRepository documentTypeRepository,
    		ScholarsRepository scholarRepo) {
        this.documentRepository = documentRepository;
        this.scholarRepo=scholarRepo;
        this.documentTypeRepository=documentTypeRepository;
    }

    public DocumentResponse uploadDocument(
            int userId,
            MultipartFile file,
            String reportId,
            String module,
            
            DocumentRequest request) throws IOException {
    	

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        
        validateModule(module);
        Scholars scholar = scholarRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Scholar not found"));

        
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = UUID.randomUUID() + "_" +
                file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir + fileName);

        Files.copy(file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);
        String relatedtable="";
        if (module.equalsIgnoreCase("progress"))
        	relatedtable="Progress_report";
        
        	
        
        DocumentType type = documentTypeRepository
                .findById(request.getDocumentTypeId())
                .orElseThrow(() -> new RuntimeException("Invalid Document Type"));
        
        	        System.out.println("Document type found: " );
        	        Document doc = new Document();
        	        doc.setScholarId(scholar.getScholarId());
        	        doc.setRelatedTable(relatedtable);
        	        doc.setRelatedId(Integer.parseInt(reportId));
        	        
        	        doc.setName(file.getOriginalFilename());
        	        doc.setStorageUri(filePath.toString());
        	        doc.setDocumentType(type);
        	        Document saved = documentRepository.save(doc);
        	

        

        return new DocumentResponse(
                saved.getDocumentId(),
                saved.getName(),
                saved.getDocumentType().getId(),
                saved.getDocumentType().getName(),
                saved.getUploadedAt().toString()
        );
    }
    @Transactional
    public List<DocumentResponse> getDocuments(
            Integer userId,
            String relatedTable,
            Integer relatedId,
            Integer scholarId) {
    	
    	
    	
    	Integer finalScholarId;

//        List<Document> documents =
//        		documentRepository.findByRelatedTableAndRelatedIdAndScholarId(
//                        relatedTable,
//                        relatedId,
//                        sch.getScholarId()
//                );
    	 if (scholarId !=null && scholarId !=0 ) {
    		 finalScholarId=scholarId;
    		 
    	    } else {
    	    	
    	    	Scholars sch = scholarRepo.findByUserId(userId)
    					.orElseThrow(() -> new IllegalArgumentException("Scholar not found: "));
    	    	finalScholarId=sch.getScholarId();
    	    }
    	 List<Document> documents =
	          		documentRepository.findByRelatedTableAndRelatedIdAndScholarId(
	                          relatedTable,
	                          relatedId,
	                          finalScholarId
	                  );
      

        return documents.stream()
                .map(doc -> new DocumentResponse(
                        doc.getDocumentId(),
                        doc.getName(),
                        doc.getDocumentType().getId(),
                        doc.getDocumentType().getName()  ,
                        doc.getUploadedAt().toString()
                       
                ))
                .collect(Collectors.toList());
    }
    
    private void validateModule(String module) {

        List<String> allowed = List.of(
                "progress",
                "thesis",
                "admission"
        );

        if (!allowed.contains(module)) {
            throw new IllegalArgumentException("Invalid module");
        }
    }
    
    public ResponseEntity<Resource> downloadDocument(Document doc) {

        try {

            Path path = Paths.get(doc.getStorageUri());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + doc.getName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error downloading file", e);
        }
    }
}
