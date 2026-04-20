package edu.dei.examination.phd.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.dto.FacultyDTO;
import edu.dei.examination.phd.model.Faculty;


@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    // Find by exact faculty name
    Optional<Faculty> findByName(String facultyName);

    // Case-insensitive search
    List<Faculty> findByNameContainingIgnoreCase(String keyword);

    // Check if exists
    boolean existsByName(String facultyName);
    
    @Query("SELECT new edu.dei.examination.phd.dto.FacultyDTO(f.id, f.name) FROM Faculty f")
    List<FacultyDTO> findAllAsDTO();
    
    
    
}