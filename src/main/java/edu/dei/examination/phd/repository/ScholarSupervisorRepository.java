package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScholarSupervisorRepository
        extends JpaRepository<ScholarSupervisor, Integer> {

    List<ScholarSupervisor> findByScholar_ScholarIdAndIsActiveTrue(Integer scholarId);
//scholarId
//    Optional<ScholarSupervisor> findByScholarIdAndRoleAndIsActiveTrue(
//            Integer scholarId,
//            SupervisorRole role
//    );

    List<ScholarSupervisor> findBySupervisorIdAndIsActiveTrue(Integer supervisorId);
    
    @Query(
    	"	SELECT s FROM ScholarSupervisor s " +
    	"	JOIN s.scholar sch " +
    	"	WHERE LOWER(sch.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
    		)
    		List<ScholarSupervisor> searchByScholarName(String keyword);
    
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
        
        @Query(
        		" SELECT s FROM ScholarSupervisor s " +
        		" JOIN s.scholar sch " +
        		" JOIN s.supervisor sup " +
        		" WHERE LOWER(sch.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        		 "  OR LOWER(sup.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
        		)
        		List<ScholarSupervisor> searchAssignments(@Param("keyword") String keyword);

}