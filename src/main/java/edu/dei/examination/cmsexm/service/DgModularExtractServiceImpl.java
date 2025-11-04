package edu.dei.examination.cmsexm.service;

import edu.dei.examination.cmsexm.model.DgModularSem;
import edu.dei.examination.cmsexm.repository.DgModularSemRepository;
import edu.dei.examination.cmsexm.repository.DgModularMainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DgModularExtractServiceImpl implements DgModularExtractService {

    private final DgModularSemRepository modularSemRepository;
    private final DgModularMainRepository modularMainRepository;

    public DgModularExtractServiceImpl(DgModularSemRepository modularSemRepository,
                                       DgModularMainRepository modularMainRepository) {
        this.modularSemRepository = modularSemRepository;
        this.modularMainRepository = modularMainRepository;
    }

    @Override
    public List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndSessionStartDate(
            String programId, String moduleGroup, Date sessionStartDate) {

        // ✅ Step 1: Fetch pending semesters (both sem + controller have status = 'N')
        List<DgModularSem> semesters = modularSemRepository.findByProgramIdAndModuleGroupAndSessionStartDate(programId, moduleGroup, sessionStartDate);

        if (!semesters.isEmpty()) {
            // ✅ Step 2: CSV generation logic here
            // (your existing CSV generation implementation)

            // ✅ Step 3: After successful CSV generation, mark both statuses = 'C'
            Date now = new Date();
            modularMainRepository.updateControllerStatusToCompleted(now, programId, moduleGroup, sessionStartDate);
            modularSemRepository.updateModularSemStatusToCompleted(programId, moduleGroup, sessionStartDate);
        }

        return semesters;
    }
}
