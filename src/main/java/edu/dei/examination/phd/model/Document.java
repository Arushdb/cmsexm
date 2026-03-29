package edu.dei.examination.phd.model;

import javax.persistence.*;
import java.time.LocalDateTime;

 

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "scholar_id", nullable = false)
    private Integer scholarId;

    @Column(name = "related_table", nullable = false)
    private String relatedTable;

    @Column(name = "related_id", nullable = false)
    private Integer relatedId;

   // @Column(name = "document_type_id")
   // private Integer documentTypeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id" )
    
    private DocumentType documentType;

    @Column(name = "name")
    private String name;

    @Column(name = "storage_uri")
    private String storageUri;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onUpload() {
        this.uploadedAt = LocalDateTime.now();
    }

	public Integer getDocumentId() { return documentId; }

	public void setDocumentId(Integer documentId) { this.documentId = documentId; }

	public Integer getScholarId() { return scholarId; }

	public void setScholarId(Integer scholarId) { this.scholarId = scholarId; }

	public String getRelatedTable() { return relatedTable; }

	public void setRelatedTable(String relatedTable) { this.relatedTable = relatedTable; }

	public Integer getRelatedId() { return relatedId; }

	public void setRelatedId(Integer relatedId) { this.relatedId = relatedId; }

	//public Integer getDocumentTypeId() { return documentTypeId; }

	//public void setDocumentTypeId(Integer documentTypeId) { this.documentTypeId = documentTypeId; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getStorageUri() { return storageUri; }

	public void setStorageUri(String storageUri) { this.storageUri = storageUri; }

	public LocalDateTime getUploadedAt() { return uploadedAt; }

	public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
	
	

	public DocumentType getDocumentType() { return documentType; }

	public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    
    // Getters & Setters
    
    
}
