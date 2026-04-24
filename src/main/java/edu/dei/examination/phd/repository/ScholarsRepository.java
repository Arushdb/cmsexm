package edu.dei.examination.phd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.phd.model.Scholars;

import java.util.List;
import java.util.Optional;

public interface ScholarsRepository extends JpaRepository<Scholars, Integer> {

    /* ---------- BASIC FINDS ---------- */

    Optional<Scholars> findByScholarId(Integer scholarId);

    Optional<Scholars> findByUserId(Integer userId);

    Optional<Scholars> findByEmail(String email);

    Optional<Scholars> findByRegistrationNo(String registrationNo);

    Optional<Scholars> findByEnrolmentno(Integer enrolmentno);

    Optional<Scholars> findByApplicationNumber(String applicationNumber);

    /* ---------- COMBINED LOGIC ---------- */

   
    /* ---------- FILTERS ---------- */

    List<Scholars> findByProgramId(Integer programId);

    List<Scholars> findByStatusId(Integer statusId);

    List<Scholars> findByDepartmentCode(String departmentCode);

    /* ---------- SUPERVISOR RELATED ---------- */

    List<Scholars> findByPrimarySupervisorId(Integer supervisorId);

    List<Scholars> findByCoSupervisorId(Integer coSupervisorId);

    /* ---------- VALIDATION HELPERS ---------- */

    boolean existsByRegistrationNo(String registrationNo);

    boolean existsByEnrolmentno(Integer enrolmentno);

    boolean existsByApplicationNumber(String applicationNumber);

    boolean existsByEmail(String email);
    
    @Query("SELECT s FROM Scholars s WHERE " +
            "(:keyword IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:deptId IS NULL OR s.department.departmentId = :deptId)")
     Page<Scholars> search(String keyword, Integer deptId, Pageable pageable);

     @Query("SELECT COUNT(s) FROM Scholars s")
     Long totalScholars();

//     @Query("SELECT new edu.dto.CountDTO(d.departmentName, COUNT(s)) " +
//            "FROM Scholar s JOIN Department d ON s.departmentId = d.id GROUP BY d.departmentName")
//     List<CountDTO> deptStats();
    
}
