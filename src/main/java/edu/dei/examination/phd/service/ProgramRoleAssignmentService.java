package edu.dei.examination.phd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.phd.model.Program;
import edu.dei.examination.phd.model.ProgramRoleAssignment;
import edu.dei.examination.phd.repository.ProgramRepository;
import edu.dei.examination.phd.repository.ProgramRoleAssignmentRepository;

@Service
public class ProgramRoleAssignmentService {

    @Autowired
    private ProgramRoleAssignmentRepository repo;
    
    @Autowired
    private ProgramRepository programRepository;
    
    @Autowired
    private RoleRepository roleRepo;
    
    private UserRepository userRepo;

    // =========================
    // ASSIGN ROLE TO PROGRAM
    // =========================
    public void assignRole(Integer userId, Integer programId, String role) {

        // prevent duplicate
        List<ProgramRoleAssignment> existing =
                repo.findByProgram_ProgramIdAndRole(programId, role);

        boolean alreadyExists = existing.stream()
                .anyMatch(e -> e.getUser().getId().equals(userId));

        if (alreadyExists) {
            throw new RuntimeException("Already assigned");
        }
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        ProgramRoleAssignment p = new ProgramRoleAssignment();
        p.setUser(user);
        
        Program program = programRepository.findByProgramId(programId)
        		       .orElseThrow(() -> new RuntimeException("Program not found"));
        
         
      Role   role1 = roleRepo.findByName(ERole.valueOf(role))
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        

        p.setProgram(program);
        
       
        p.setProgram(program);
        p.setRole(role);

        repo.save(p);
    }

    // =========================
    // GET USERS BY PROGRAM + ROLE
    // =========================
    public List<ProgramRoleAssignment> getByProgramAndRole(Integer programId, String role) {
        return repo.findByProgram_ProgramIdAndRole(programId, role);
    }

    // =========================
    // REMOVE
    // =========================
    public void remove(Integer id) {
        repo.deleteById(id);
    }
}