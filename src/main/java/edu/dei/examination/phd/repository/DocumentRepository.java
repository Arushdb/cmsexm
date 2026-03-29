package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.phd.model.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByRelatedTableAndRelatedIdAndScholarId(
            String relatedTable,
            Integer relatedId,
            Integer scholarId
    );
    
    
    @Query(
    		" SELECT d FROM Document d"+
    		" JOIN FETCH d.documentType dt "+
    		" WHERE d.relatedTable = :relatedTable"+
    		" AND d.relatedId = :relatedId"+
    		" AND d.scholarId = :scholarId"
    		)
    		List<Document> findDocumentsWithType(
    		        String relatedTable,
    		        Integer relatedId,
    		        Integer scholarId);
}
