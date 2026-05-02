package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.phd.dto.DepartmentDTO;
import edu.dei.examination.phd.model.Department;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // Find by exact name
    Optional<Department> findByDepartmentName(String departmentName);

    // Case-insensitive search (useful for Angular search)
    List<Department> findByDepartmentNameContainingIgnoreCase(String keyword);

    // Check existence
    boolean existsByDepartmentName(String departmentName);
    
 // Get departments by faculty ID
    List<Department> findByFaculty_Id(Integer facultyId);
    
    @Query("SELECT d FROM Department d JOIN FETCH d.faculty")
    List<Department> findAllWithFaculty();
    
    @Query("SELECT new edu.dei.examination.phd.dto.DepartmentDTO(d.id, d.departmentName, d.faculty.id, d.faculty.name) FROM Department d")
    List<DepartmentDTO> findAllAsDTO();
    
    @Query("SELECT d FROM Department d WHERE d.faculty.id = :facultyId AND LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Department> searchByFaculty(Integer facultyId, String keyword);
    
    @Query("SELECT d FROM Department d WHERE LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Department> searchByName(String name);
    
    @Query("SELECT d FROM Department d WHERE d.faculty.id = :facultyId AND LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Department> searchByNameAndFaculty(Integer facultyId, String name);
    
   Optional<Department> findByDepartmentCode(String code); 
}