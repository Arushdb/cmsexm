package edu.dei.examination.phd.dto;

public class DocumentResponse {

    private Integer documentId;
    private String name;
    private Integer documentTypeId;
    private String uploadedAt;
    private String documentTypeName;

    public DocumentResponse(
            Integer documentId,
            String name,
            Integer documentTypeId,
            String documentTypeName,
            String uploadedAt) {

        this.documentId = documentId;
        this.name = name;
        this.documentTypeId = documentTypeId;
        this.uploadedAt = uploadedAt;
        this.documentTypeName=documentTypeName;
    }

	public Integer getDocumentId() { return documentId; }

	public void setDocumentId(Integer documentId) { this.documentId = documentId; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public Integer getDocumentTypeId() { return documentTypeId; }

	public void setDocumentTypeId(Integer documentTypeId) { this.documentTypeId = documentTypeId; }

	public String getUploadedAt() { return uploadedAt; }

	public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }

	public String getDocumentTypeName() { return documentTypeName; }

	public void setDocumentTypeName(String documentTypeName) { this.documentTypeName = documentTypeName; }
	

    // getters
    
}
