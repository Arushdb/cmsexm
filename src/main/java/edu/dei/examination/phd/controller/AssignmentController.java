package edu.dei.examination.phd.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.DepartmentRoleDTO;
import edu.dei.examination.phd.dto.DocumentResponse;
import edu.dei.examination.phd.dto.ProgramRoleDTO;
import edu.dei.examination.phd.dto.ScholarDTO;
import edu.dei.examination.phd.dto.ScholarDashboardDTO;
import edu.dei.examination.phd.model.DepartmentRoleAssignment;
import edu.dei.examination.phd.model.ProgramRoleAssignment;
import edu.dei.examination.phd.service.AssignmentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assign")
@CrossOrigin(origins = "http://localhost:4200")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // =========================
    // ✅ ASSIGN PROGRAM ROLE ( REVIEWER)
    // =========================
    @PostMapping("/program-role")
    public ResponseEntity<?> assignProgramRole(@RequestBody Map<String, Object> req) {

        Integer userId = (Integer) req.get("userId");
        Integer programId = (Integer) req.get("programId");
        String role = (String) req.get("role");

        assignmentService.assignProgramRole(userId, programId, role);
        
        
        return ResponseEntity.ok(
                ApiResponse.success("Program role assigned successfully", null));

    }
    
  
    // =========================
    // ✅ ASSIGN SUPERVISOR (SCHOLAR LEVEL)
    // =========================
    @PostMapping("/supervisor")
    public ResponseEntity<?> assignSupervisor(@RequestBody Map<String, Integer> req) {

        Integer scholarId = req.get("scholarId");
        Integer userId = req.get("userId");

        assignmentService.assignSupervisor(scholarId, userId);
//        public static <T> ApiResponse<T> success(String msg, T data) {
//            return new ApiResponse<>(true, msg, data, null);
//        }
        
       // ApiResponse<> response = new ApiResponse<>("Scholar profile fetched successfully", null);
				
        
        //return ResponseEntity.ok("Supervisor assigned successfully");
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Supervisor assigned successfully", null, null)
            );
    }
    
    @GetMapping("/allsupervisors")
    public ResponseEntity<?> getAllSupervisors() {

        return ResponseEntity.ok(
                assignmentService.getAllSupervisorAssignments()
        );
    }

    // =========================
    // ✅ GET PROGRAM ROLE (HOD / DEAN / REVIEWER)s
    // =========================
    @GetMapping("/program-role")
    public ResponseEntity<?> getProgramRoles(
            @RequestParam Integer programId,
            @RequestParam String role) {

        return ResponseEntity.ok(
                assignmentService.getByProgramAndRole(programId, role)
        );
    }

    @GetMapping("/allprogramroles")
    public ResponseEntity<ApiResponse<List<ProgramRoleDTO>>> getAllProgramroles() {
    	
    	
    	List<ProgramRoleAssignment> list =assignmentService.getAllProgramRole();
            
        
    List<ProgramRoleDTO>  listProgramrole=	
    		list.stream().map(s-> new ProgramRoleDTO(
    		s.getId(),		
    		s.getUser().getId(),
    		s.getProgram().getProgramId(),
    		s.getUser().getName().toString(),
    		s.getProgram().getProgramname().toString(),
    		s.getRole().toString()
    		)).collect(Collectors.toList() );
    				
    	
    	

        return ResponseEntity.ok(new ApiResponse<>(true,"Program roles fetched successfully",listProgramrole,null));
               
        
              
    }
   
    @GetMapping("/allhodroles")
    public ResponseEntity<ApiResponse<List<DepartmentRoleDTO>>> getAllHodroles() {
     	
    	List<DepartmentRoleDTO> list =assignmentService.getAllHodRole();
    				
   
        return ResponseEntity.ok(new ApiResponse<>(true,"Department roles fetched successfully",
        		list,null));
               
        
              
    }
    
    
    
    @GetMapping("/alldeanroles")
    public ResponseEntity<ApiResponse<List<DepartmentRoleDTO>>> getAllDeanroles() {
     	
    	List<DepartmentRoleDTO> list =assignmentService.getAllDeanRoles();
    				
   
        return ResponseEntity.ok(new ApiResponse<>(true,"Dean  roles fetched successfully",
        		list,null));
               
        
              
    }
    
    @DeleteMapping("/hod/{id}")
    public ResponseEntity<ApiResponse<?>> removeHod(@PathVariable Integer id) {
     	
    	assignmentService.removeHodById(id);
    				
   
    	return ResponseEntity.ok(ApiResponse.success("HOD removed", null));
    			
      }
    
    @DeleteMapping("/dean/{id}")
    public ResponseEntity<ApiResponse<?>> removeDean(@PathVariable Integer id) {
     	
    	assignmentService.removeDeanById(id);
    				
   
    	return ResponseEntity.ok(ApiResponse.success("Dean removed", null));
    			
      }
    
    // =========================
    // ✅ GET SUPERVISORS BY SCHOLAR
    // =========================
    @GetMapping("/supervisor/{scholarId}")
    public ResponseEntity<?> getSupervisors(@PathVariable Integer scholarId) {

        return ResponseEntity.ok(
                assignmentService.getSupervisors(scholarId)
        );
    }

    // =========================
    // ✅ DELETE PROGRAM ROLE
    // =========================
    @DeleteMapping("/reviewer/{id}")
    public ResponseEntity<?> deleteProgramRole(@PathVariable Integer id) {

        assignmentService.removeProgramRole(id);
        

        return ResponseEntity.ok(ApiResponse.success("Program role deleted successfully",null));
    }

    // =========================
    // ✅ DELETE SUPERVISOR
    // =========================
    @DeleteMapping("/supervisor/{id}")
    public ResponseEntity<?> deleteSupervisor(@PathVariable Integer id) {

        assignmentService.removeSupervisor(id);
        
        

        return ResponseEntity.ok(new ApiResponse<>(true, "Supervisor removed successfully", null, null));
    }
    
    @PutMapping("/supervisor/{id}")
    public ResponseEntity<?> updateSupervisor(@PathVariable Integer id,
                                              @RequestBody Map<String, Integer> req) {

        Integer userId = req.get("userId");

        assignmentService.updateSupervisor(id, userId);

        return ResponseEntity.ok("Updated successfully");
    }
    
    @GetMapping("/supervisor/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        return ResponseEntity.ok(
                assignmentService.searchAssignments(keyword)
        );
    }
    
    @GetMapping("/program-role/search")
    public ResponseEntity<?> searchProgramRoles(
            @RequestParam String keyword,
            @RequestParam String role) {

        return ResponseEntity.ok(
                assignmentService.searchProgramRoles(keyword, role)
        );
    }
    
    @GetMapping("/hod/search")
    public ResponseEntity<?> searchHodRoles(
            @RequestParam String keyword
            ) {

        return ResponseEntity.ok(
                assignmentService.searchHodRoles(keyword)
        );
    }
    
    @GetMapping("/dean/search")
    public ResponseEntity<?> searchDeanRoles(
            @RequestParam String keyword
            ) {

        return ResponseEntity.ok(
                assignmentService.searchDeanRoles(keyword)
        );
    }
    
    // =========================
    // ASSIGN HOD
    // =========================
    @PostMapping("/hod")
    public ResponseEntity<?> assignHod(@RequestBody Map<String, Integer> req) {
    	
    	

    	assignmentService.assignHod(req.get("departmentId"), req.get("userId"));

       
        return ResponseEntity.ok(ApiResponse.success("HOD assigned successfully",null));
    }

    // =========================
    // ASSIGN DEAN
    // =========================
    @PostMapping("/dean")
    public ResponseEntity<?> assignDean(@RequestBody Map<String, Integer> req) {

    	assignmentService.assignDean(req.get("facultyId"), req.get("userId"));

        return ResponseEntity.ok( ApiResponse.success("Dean assigned successfully",null));
    }
    
    
}
