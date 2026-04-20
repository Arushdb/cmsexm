package edu.dei.examination.phd.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.dto.DepartmentRoleDTO;
import edu.dei.examination.phd.model.FacultyRoleAssignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRoleRepository
        extends JpaRepository<FacultyRoleAssignment, Integer> {

    // =========================
    // ✅ FIND DEAN BY FACULTY
    // =========================
    Optional<FacultyRoleAssignment> findByFaculty_IdAndRole(Integer facultyId, String role);

    // =========================
    // ✅ GET ALL DEANS
    // =========================
    List<FacultyRoleAssignment> findByRole(String role);
   
    
        
    @Query(" SELECT new edu.dei.examination.phd.dto.DepartmentRoleDTO(" +
    	       " fra.id,u.name, u.id,  f.id, f.name, fra.role ) " +
    	       " FROM FacultyRoleAssignment fra " +
    	       " JOIN fra.user u " +
    	       " JOIN fra.faculty f " +
    	       " WHERE fra.role = 'ROLE_DEAN'")
    	List<DepartmentRoleDTO> getAllDeanRoles();

    // =========================
    // ✅ DELETE BY FACULTY + ROLE
    // =========================
    void deleteByFaculty_IdAndRole(Integer facultyId, String role);
}