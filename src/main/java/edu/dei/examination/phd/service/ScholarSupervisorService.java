package edu.dei.examination.phd.service;

import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScholarSupervisorService {

    private final ScholarSupervisorRepository scholarSupervisorRepository;
    private final ScholarsRepository scholarsRepository;
    

    public ScholarSupervisorService(
            ScholarSupervisorRepository scholarSupervisorRepository,ScholarsRepository scholarsRepository) {

        this.scholarSupervisorRepository = scholarSupervisorRepository;
        this.scholarsRepository=scholarsRepository;
    }

    // Get all supervisors of a scholar
//    public List<ScholarSupervisor> getSupervisorsByScholar(Scholars Scholar) {
//
//        return scholarSupervisorRepository
//                .findByScholarIdAndIsActiveTrue(Scholar);
//    }

    // Get primary supervisor of scholar
//    public ScholarSupervisor getPrimarySupervisor(Integer scholarId) {
//
//        return scholarSupervisorRepository
//                .findByScholarIdAndRoleAndIsActiveTrue(
//                        scholarId,
//                        SupervisorRole.PRIMARY
//                )
//                .orElseThrow(() ->
//                        new RuntimeException("Primary supervisor not assigned"));
//    }

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
        Scholars scholar =scholarsRepository.findByScholarId(scholarId).orElseThrow();

        ss.setScholar(scholar);
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