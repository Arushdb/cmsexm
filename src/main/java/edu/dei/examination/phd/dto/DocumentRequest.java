package edu.dei.examination.phd.dto;

public class DocumentRequest {

    private Integer relatedId;
    private Integer documentTypeId;

    // Optional if you want generic module support
    private String relatedTable;

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public void setRelatedTable(String relatedTable) {
        this.relatedTable = relatedTable;
    }
}

