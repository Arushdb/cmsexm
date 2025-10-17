package edu.dei.examination.cmsexm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.DgModularSem;
import edu.dei.examination.cmsexm.repository.DgModularSemRepository;

@Service
public class DgModularExtractServiceImpl implements DgModularExtractService {

    private final DgModularSemRepository modularSemRepository;

    public DgModularExtractServiceImpl(DgModularSemRepository modularSemRepository) {
        this.modularSemRepository = modularSemRepository;
    }

   
    @Override
    public List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndStatus(String programId, String moduleGroup, String status) {
        return modularSemRepository.findByProgramIdAndModuleGroupAndStatus(programId, moduleGroup, status);
    }
}
