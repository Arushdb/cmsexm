package edu.dei.examination.phd.repository;

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

    @Query(
        "SELECT s FROM Scholars s " +
        "WHERE s.registrationNo = :loginId " +
        "   OR s.enrolmentno = :enrolmentNo " +
        "   OR s.applicationNumber = :loginId " +
        "   OR s.email = :email"
    )
    Optional<Scholars> findScholarForLogin(
            @Param("loginId") String loginId,
            @Param("enrolmentNo") Integer enrolmentNo,
            @Param("email") String email
    );

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
}
