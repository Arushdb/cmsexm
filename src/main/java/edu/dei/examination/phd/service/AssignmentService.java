package edu.dei.examination.phd.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.cmsexm.model.*;
import edu.dei.examination.cmsexm.repository.*;

import edu.dei.examination.phd.dto.DepartmentRoleDTO;
import edu.dei.examination.phd.dto.ProgramRoleDTO;
import edu.dei.examination.phd.dto.SupervisorAssignmentDTO;
import edu.dei.examination.phd.enums.SupervisorRole;
import edu.dei.examination.phd.model.Department;
import edu.dei.examination.phd.model.DepartmentRoleAssignment;
import edu.dei.examination.phd.model.Faculty;
import edu.dei.examination.phd.model.FacultyRoleAssignment;
import edu.dei.examination.phd.model.Program;
import edu.dei.examination.phd.model.ProgramRoleAssignment;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.model.Supervisor;
import edu.dei.examination.phd.repository.DepartmentRepository;
import edu.dei.examination.phd.repository.DepartmentRoleAssignmentRepository;
import edu.dei.examination.phd.repository.FacultyRepository;
import edu.dei.examination.phd.repository.FacultyRoleRepository;
import edu.dei.examination.phd.repository.ProgramRepository;
import edu.dei.examination.phd.repository.ProgramRoleAssignmentRepository;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SupervisorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired private ProgramRoleAssignmentRepository programRoleRepo;
    @Autowired private ScholarSupervisorRepository scholarsupervisorRepo;
    @Autowired private SupervisorRepository supervisorRepo;
    

    @Autowired private ProgramRepository programRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ScholarsRepository scholarRepo;
    @Autowired private DepartmentRepository departmentRepo;
    @Autowired private DepartmentRoleAssignmentRepository departmentRoleRepo;
    @Autowired private FacultyRepository facultyRepo;
    @Autowired private FacultyRoleRepository facultyroleRepo;
    

    // =========================
    // ASSIGN PROGRAM ROLE ( REVIEWER)
    // =========================
    public void assignProgramRole(Integer userId, Integer programId, String role) {

        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent duplicate
        boolean exists = programRoleRepo
                .findByProgram_ProgramIdAndRole(programId, role)
             
                .stream()
                .anyMatch(e -> e.getUser().getId().equals(userId));

        if (exists) {
            throw new RuntimeException(role + " already assigned to this program");
        }

        ProgramRoleAssignment p = new ProgramRoleAssignment();
        p.setProgram(program);
        p.setUser(user);
        p.setRole(role);

        programRoleRepo.save(p);
    }
    
    public void assignHod(Integer departmentId, Integer userId) {

        Department dept = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Only ONE HOD per department
        Optional<DepartmentRoleAssignment> existing =
        		departmentRoleRepo.findByDepartment_DepartmentIdAndRole(departmentId, "ROLE_HOD");

        DepartmentRoleAssignment assignment;
        

        if (existing.isPresent()) {
            // update existing HOD
            assignment = existing.get();
            assignment.setUser(user);
            assignment.setIsActive(true);
        } else {
            assignment = new DepartmentRoleAssignment();
            assignment.setDepartment(dept);
            assignment.setUser(user);
            assignment.setRole("ROLE_HOD");
            assignment.setIsActive(true);
            
        }

        departmentRoleRepo.save(assignment);
    }
    public void assignDean(Integer facultyId, Integer userId) {

        Faculty faculty = facultyRepo.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Only ONE Dean per faculty
        Optional<FacultyRoleAssignment> existing =
        		facultyroleRepo.findByFaculty_IdAndRole(facultyId, "ROLE_DEAN");

        FacultyRoleAssignment assignment;

        if (existing.isPresent()) {
            assignment = existing.get();
            assignment.setUser(user);
        } else {
            assignment = new FacultyRoleAssignment();
            assignment.setFaculty(faculty);
            assignment.setUser(user);
            assignment.setRole("ROLE_DEAN");
        }

        facultyroleRepo.save(assignment);
    }
    


    // =========================
    // ASSIGN SUPERVISOR (SCHOLAR LEVEL)
    // =========================
    @Transactional
    public void assignSupervisor(Integer scholarId, Integer uesrId) {

        Scholars scholar = scholarRepo.findById(scholarId)
                .orElseThrow(() -> new RuntimeException("Scholar not found"));

        User user = userRepo.findById(uesrId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));
        
//        Supervisor supervisor =supervisorRepo.findByUserId(uesrId)
//        		 .orElseThrow(() -> new RuntimeException("Supervisor  not found"));;
//        		
        		;

        // prevent duplicate
        boolean exists = scholarsupervisorRepo.findByScholar_ScholarIdAndIsActiveTrue(scholarId)
                .stream()
                .anyMatch(s -> s.getSupervisor().getId().equals(uesrId));

        if (exists) {
            throw new RuntimeException("Supervisor already assigned");
        }

        ScholarSupervisor s = new ScholarSupervisor();
        s.setScholar(scholar);
        s.setSupervisor(user);
        s.setRole(SupervisorRole.PRIMARY);

        scholarsupervisorRepo.save(s);
        DepartmentRoleAssignment assignment;
        departmentRoleRepo.findByDepartment_DepartmentIdAndUser_IdAndRole
        (scholar.getDepartment().getDepartmentId(), user.getId(), "ROLE_SUPERVISOR");
        assignment = new DepartmentRoleAssignment();
        assignment.setDepartment(scholar.getDepartment());
        assignment.setUser(user);
        assignment.setRole("ROLE_SUPERVISOR");
        assignment.setIsActive(true);
        departmentRoleRepo.save(assignment);
        
        
        
    }

    // =========================
    // GET PROGRAM ROLE ASSIGNMENTS
    // =========================
    public List<ProgramRoleAssignment> getByProgramAndRole(Integer programId, String role) {
        return programRoleRepo.findByProgram_ProgramIdAndRole(programId, role);
    }

    // =========================
    // GET SUPERVISORS BY SCHOLAR
    // =========================
    public List<ScholarSupervisor> getSupervisors(Integer scholarId) {
        return scholarsupervisorRepo.findByScholar_ScholarIdAndIsActiveTrue(scholarId);
    }

    // =========================
    // REMOVE PROGRAM ROLE
    // =========================
    public void removeProgramRole(Integer id) {
        programRoleRepo.deleteById(id);
    }

    // =========================
    // REMOVE SUPERVISOR
    // =========================
    public void removeSupervisor(Integer id) {
    	scholarsupervisorRepo.deleteById(id);
    }

    public List<ProgramRoleAssignment> getAllProgramRole() {
        return programRoleRepo.findAllWithDetails();
        		
        		
    }
    
    public void updateSupervisor(Integer id, Integer userId) {

        ScholarSupervisor s = scholarsupervisorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        //s.setSupervisorId(userId);
        
        User supervisor = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));
        s.setSupervisor(supervisor);

        scholarsupervisorRepo.save(s);
    }
    
    public List<SupervisorAssignmentDTO> searchAssignments(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return scholarsupervisorRepo.findAll()
            		.stream()
            		 .map(s -> {

                         SupervisorAssignmentDTO dto = new SupervisorAssignmentDTO();

                         dto.setId(s.getId());

                         dto.setScholarId(s.getScholar().getScholarId());
                         dto.setScholarName(s.getScholar().getFullName());
                         dto.setEnrollmentNo(s.getScholar().getEnrolmentno());
                         dto.setSupervisorName(s.getSupervisor().getName());

                         dto.setSupervisorId(s.getSupervisor().getId()); // or s.getSupervisor().getId()

                         // if using User entity:
                         // dto.setSupervisorName(s.getSupervisor().getName());

                         return dto;
                     })
                     .toList();// fallback
        }
        List<ScholarSupervisor> list =scholarsupervisorRepo.searchAssignments(keyword.trim());
        return scholarsupervisorRepo.searchAssignments(keyword.trim())
        		.stream()
        		 .map(s -> {

                     SupervisorAssignmentDTO dto = new SupervisorAssignmentDTO();

                     dto.setId(s.getId());

                     dto.setScholarId(s.getScholar().getScholarId());
                     dto.setScholarName(s.getScholar().getFullName());
                     dto.setEnrollmentNo(s.getScholar().getEnrolmentno());
                     dto.setSupervisorName(s.getSupervisor().getName());

                     dto.setSupervisorId(s.getSupervisor().getId()); // or s.getSupervisor().getId()

                     // if using User entity:
                     // dto.setSupervisorName(s.getSupervisor().getName());

                     return dto;
                 })
                 .toList();
    }
    
    
    public List<SupervisorAssignmentDTO> getAllSupervisorAssignments() {

        return scholarsupervisorRepo.findAll()
                .stream()
                .map(s -> {

                    SupervisorAssignmentDTO dto = new SupervisorAssignmentDTO();

                    dto.setId(s.getId());

                    dto.setScholarId(s.getScholar().getScholarId());
                    dto.setScholarName(s.getScholar().getFullName());
                    dto.setEnrollmentNo(s.getScholar().getEnrolmentno());
                    dto.setSupervisorName(s.getSupervisor().getName());
                    dto.setUserId(s.getSupervisor().getId());

                    //dto.setSupervisorId(s.getSupervisor().getId()); // or s.getSupervisor().getId()

                    // if using User entity:
                    // dto.setSupervisorName(s.getSupervisor().getName());

                    return dto;
                })
                .toList();
    }
    
    
    @Transactional
    public List<ProgramRoleDTO> searchProgramRoles(String keyword, String role) {

    	
       return  programRoleRepo.searchByKeywordAndRole(keyword, role)
         .stream()
         .map(s->new ProgramRoleDTO(
        		 s.getId(),
        		 s.getUser().getId(),
        		 s.getProgram().getProgramId(),
        		 s.getUser().getName(),
        		 s.getProgram().getProgramname(),
        		 ""
        		 )).collect(Collectors.toList());
        	 
         
         
         
         
    }

	@Transactional
    public List<DepartmentRoleDTO> getAllHodRole() { 
		// TODO Auto-generated method stub
	      List<DepartmentRoleAssignment> listhod = 
	    		  departmentRoleRepo.findByRole("ROLE_HOD");
	    		  
	
	List<DepartmentRoleDTO>  listDTO=	
			listhod.stream().map(s-> new DepartmentRoleDTO(
    		s.getId(),	
    		s.getDepartment().getDepartmentId(),
    		s.getDepartment().getDepartmentName(),
    		s.getUser().getName(),
    		s.getUser().getId()
    		
    		)).collect(Collectors.toList() );
		
		return listDTO; 
	
	
	}

	public void removeHodById(Integer id) {
		
		 DepartmentRoleAssignment entity = departmentRoleRepo.findById(id)
			        .orElseThrow(() -> new RuntimeException("Department HOD not found"));

		 departmentRoleRepo.delete(entity);
	
	
	}
	
	public void removeDeanById(Integer id) {
		
		 FacultyRoleAssignment entity =facultyroleRepo.findById(id)
			        .orElseThrow(() -> new RuntimeException("Faculty Dean not found"));

		 facultyroleRepo.delete(entity);
	
	
	}

	public List<DepartmentRoleDTO> getAllDeanRoles() { // TODO Auto-generated method stub
		
	   return  facultyroleRepo.getAllDeanRoles();
	 }

	public List<DepartmentRoleDTO>  searchHodRoles(String keyword) {
		// TODO Auto-generated method stub
		return departmentRoleRepo.searchByKeyword(keyword);
	}

	public List<DepartmentRoleDTO> searchDeanRoles(String keyword) {
		// TODO Auto-generated method stub
		return departmentRoleRepo.searchFacultyRoles(keyword);
	}
}
