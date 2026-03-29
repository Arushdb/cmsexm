package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.ScholarSupervisor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScholarSupervisorRepository
        extends JpaRepository<ScholarSupervisor, Integer> {

    List<ScholarSupervisor> findByScholarIdAndIsActiveTrue(Integer scholarId);

    Optional<ScholarSupervisor> findByScholarIdAndRoleAndIsActiveTrue(
            Integer scholarId,
            SupervisorRole role
    );

    List<ScholarSupervisor> findBySupervisorIdAndIsActiveTrue(Integer supervisorId);
}