package edu.dei.examination.cmsexm.service;

import java.util.List;
import java.util.Date;

import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularExtractService {

    List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndSessionStartDate(
            String programId, String moduleGroup, Date sessionStartDate);
}
