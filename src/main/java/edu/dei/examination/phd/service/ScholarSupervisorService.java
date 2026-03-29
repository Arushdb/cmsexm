package edu.dei.examination.phd.service;

import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.ScholarSupervisor;

import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScholarSupervisorService {

    private final ScholarSupervisorRepository scholarSupervisorRepository;

    public ScholarSupervisorService(
            ScholarSupervisorRepository scholarSupervisorRepository) {

        this.scholarSupervisorRepository = scholarSupervisorRepository;
    }

    // Get all supervisors of a scholar
    public List<ScholarSupervisor> getSupervisorsByScholar(Integer scholarId) {

        return scholarSupervisorRepository
                .findByScholarIdAndIsActiveTrue(scholarId);
    }

    // Get primary supervisor of scholar
    public ScholarSupervisor getPrimarySupervisor(Integer scholarId) {

        return scholarSupervisorRepository
                .findByScholarIdAndRoleAndIsActiveTrue(
                        scholarId,
                        SupervisorRole.PRIMARY
                )
                .orElseThrow(() ->
                        new RuntimeException("Primary supervisor not assigned"));
    }

    // Get scholars under a supervisor
    public List<ScholarSupervisor> getScholarsBySupervisor(Integer supervisorId) {

        return scholarSupervisorRepository
                .findBySupervisorIdAndIsActiveTrue(supervisorId);
    }

    // Assign supervisor
    public ScholarSupervisor assignSupervisor(
            Integer scholarId,
            Integer supervisorId,
            SupervisorRole role) {

        ScholarSupervisor ss = new ScholarSupervisor();

        ss.setScholarId(scholarId);
        ss.setSupervisorId(supervisorId);
        ss.setRole(role);
        ss.setIsActive(true);
        ss.setAssignedOn(LocalDate.now());

        return scholarSupervisorRepository.save(ss);
    }

    // Remove supervisor (soft delete)
    public void deactivateSupervisor(Integer id) {

        ScholarSupervisor ss = scholarSupervisorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        ss.setIsActive(false);

        scholarSupervisorRepository.save(ss);
    }

}