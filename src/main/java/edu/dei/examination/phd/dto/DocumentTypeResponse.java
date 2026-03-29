package edu.dei.examination.phd.dto;

public class DocumentTypeResponse {

    private Integer id;
    private String name;

    public DocumentTypeResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
}
