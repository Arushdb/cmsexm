package edu.dei.examination.cmsexm.service;

import java.util.Date;
import java.util.List;

import edu.dei.examination.cmsexm.model.DgModularMain;
import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularMainService {

    // ---- DgModularMain related ----
    List<DgModularMain> getDgProgramList(String status);

    List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndSessionStartDate(
            String programId, String moduleGroup, Date sessionStartDate);

    // ---- DgModularController update ----
    int updateDgModularControlStatus(Date runtime, String programId, String modularGroup, Date sessionStartDate);
    
}
