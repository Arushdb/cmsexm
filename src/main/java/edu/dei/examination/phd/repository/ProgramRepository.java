package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.model.Program;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {

    // =========================
    // FIND BY NAME
    // =========================
    Optional<Program> findByProgramname(String name);

    // =========================
    // SEARCH
    // =========================
    List<Program> findByProgramnameContainingIgnoreCase(String keyword);

    // =========================
    // FILTER BY DEPARTMENT
    // =========================
    List<Program> findByDepartmentid(Integer departmentId);

    // =========================
    // FILTER BY MODE
    // =========================
    List<Program> findByMode(String mode);
    
    Optional< Program> findByProgramId(Integer id); 

}