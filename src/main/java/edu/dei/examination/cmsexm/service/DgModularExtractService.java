package edu.dei.examination.cmsexm.service;

import java.util.List;

import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularExtractService {

  

    List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndStatus(String programId, String moduleGroup, String status);
}
