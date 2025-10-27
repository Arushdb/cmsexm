package edu.dei.examination.cmsexm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularSemRepository extends JpaRepository<DgModularSem, Integer> {

    // Fetch all by programId
  //  List<DgModularSem> findByProgramId(String programId);

    // Fetch all by programId and moduleGroup
   // List<DgModularSem> findByProgramIdAndModuleGroup(String programId, String moduleGroup);

    // Fetch all by programId, moduleGroup and active status
    List<DgModularSem> findByProgramIdAndModuleGroupAndStatus(String programId, String moduleGroup, String status);
}
