package edu.dei.examination.phd.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "document_type")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    private Boolean active = true;
    
    @OneToMany(mappedBy ="documentType" )
    private List<Document> documents; 

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public Boolean getActive() { return active; }

	public void setActive(Boolean active) { this.active = active; }

	public Integer getId() { return id; }

    // Getters & Setters
    
    
}

