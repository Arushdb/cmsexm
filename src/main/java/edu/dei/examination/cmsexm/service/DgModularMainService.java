package edu.dei.examination.cmsexm.service;

import java.util.List;

import edu.dei.examination.cmsexm.model.DgModularMain;
import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularMainService {

    // ---- DgModularMain related ----
    List<DgModularMain> getDgProgramList(String status);

   

    List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndStatus(String programId, String moduleGroup, String status);
}
