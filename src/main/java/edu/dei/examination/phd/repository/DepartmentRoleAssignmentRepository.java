package edu.dei.examination.phd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.phd.dto.DepartmentRoleDTO;
import edu.dei.examination.phd.model.DepartmentRoleAssignment;

public interface DepartmentRoleAssignmentRepository extends JpaRepository<DepartmentRoleAssignment, Integer> {

	List<DepartmentRoleAssignment> findByDepartment_DepartmentId(Integer departmentId);

//=========================
// ✅ FIND HOD BY DEPARTMENT
// =========================
	Optional<DepartmentRoleAssignment> findByDepartment_DepartmentIdAndRole(Integer departmentId, String role);

	Optional<DepartmentRoleAssignment> findByDepartment_DepartmentIdAndUser_IdAndRole(Integer departmentId,Integer id, String role);
	
	
// =========================
// ✅ GET ALL HODs
// =========================
	List<DepartmentRoleAssignment> findByRole(String role);
		
		
	
	@Query("SELECT new edu.dei.examination.phd.dto.DepartmentRoleDTO(" +
		       " dra.id,d.id,d.departmentName,u.name, u.id ) " +
		       "FROM DepartmentRoleAssignment dra " +
		       "JOIN dra.user u " +
		       "JOIN dra.department d " +
		       "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "   OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "   OR LOWER(dra.role) LIKE LOWER(CONCAT('%', :keyword, '%'))")
		List<DepartmentRoleDTO> searchByKeyword(String keyword);
	
	
	
	@Query("SELECT new edu.dei.examination.phd.dto.DepartmentRoleDTO(" +
		       " fra.id,u.name, u.id, f.id, f.name, fra.role ) " +
		       "FROM FacultyRoleAssignment fra " +
		       "JOIN fra.user u " +
		       "JOIN fra.faculty f " +
		       "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "   OR LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "   OR LOWER(fra.role) LIKE LOWER(CONCAT('%', :keyword, '%'))")
		List<DepartmentRoleDTO> searchFacultyRoles(String keyword);

}
