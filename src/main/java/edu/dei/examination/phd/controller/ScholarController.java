package edu.dei.examination.phd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.dei.examination.cmsexm.exception.ResourceNotFoundException;
import edu.dei.examination.cmsexm.security.jwt.JwtUtils;
import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.dto.ScholarDTO;
import edu.dei.examination.phd.dto.ScholarDashboardDTO;
import edu.dei.examination.phd.model.ProgramRoleAssignment;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.service.ScholarService;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/scholars")
public class ScholarController {

	@Autowired
	JwtUtils jwtUtils;

	private final ScholarService scholarService;

	public ScholarController(ScholarService scholarService) {
		this.scholarService = scholarService;
	}

	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<Integer>> generateScholars(@RequestBody CreateScholarsRequest request) {
		int count = scholarService.createScholarsForAcademicYearAndMonth(request);

		if (count == 0) {
			throw new ResourceNotFoundException("Record not found");
			// return ResponseEntity.ok( ApiResponse.success("Created scholars
			// successfully", count) );

		}

		else {
			return ResponseEntity.ok(ApiResponse.success("No scholars created", count));
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','EXAMADMIN')")
	@PutMapping("/academics/{id}")
    public ResponseEntity<ScholarDTO> updateAcademic(
            @PathVariable Integer id,
            @RequestBody ScholarDTO dto) {

        ScholarDTO updated = scholarService.updateAcademic(id, dto);

        return ResponseEntity.ok(updated);
    }

	@GetMapping("/scholardashboard")

	public ResponseEntity<ApiResponse<ScholarDashboardDTO>> dashboard(
			@RequestHeader("Authorization") String authHeader) {

		// 1️⃣ Extract JWT claims
		Claims claims = jwtUtils.getClaims(authHeader);
		String username = claims.getSubject();
		Integer userid = claims.get("userId", Integer.class);
		System.out.println(claims);

		Scholars scholar = scholarService.getScholarByUserid(userid);

		// Scholars scholar=scholarService.getScholarByUserid(1);

		// int semid =Integer.parseInt(semesterId);
		// int schid =Integer.parseInt(scholarid);

		ScholarDashboardDTO dto = scholarService.getScholarDashboard(scholar.getScholarId());

		ApiResponse<ScholarDashboardDTO> response = new ApiResponse<>(true, "Scholar profile fetched successfully", dto,
				null);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/profile")
	public ResponseEntity<?> getscholarProfile(@RequestHeader("Authorization") String authHeader) {

		// 1️⃣ Extract JWT claims
		Claims claims = jwtUtils.getClaims(authHeader);
		String username = claims.getSubject();
		Integer userid = claims.get("userId", Integer.class);
		System.out.println(claims);

		Scholars scholar = scholarService.getScholarByUserid(userid);

		ScholarDashboardDTO dto = scholarService.getScholarDashboard(scholar.getScholarId());
		ApiResponse<ScholarDashboardDTO> response = new ApiResponse<>(true, "Scholar profile fetched successfully", dto,
				null);

		return ResponseEntity.ok(response);

	}

	// =========================
	// GET ALL
	// =========================
	@GetMapping
	public  ResponseEntity<?> getAll() { 
		List<ScholarDTO> list = scholarService.getAll(); 
		ApiResponse<List<ScholarDTO>> response = new ApiResponse<>(true, "Scholars fetched successfully", list,
				null);
		return ResponseEntity.ok(response);	
	}

	// =========================
	// GET BY ID
	// =========================
	@GetMapping("/{id}")
	public Scholars getById(@PathVariable Integer id) {
		return scholarService.getById(id);
	}

	// =========================
	// UPDATE
	// =========================
	@PutMapping("/{id}")
	public Scholars update(@PathVariable Integer id, @RequestBody ScholarDTO s) {
		return scholarService.update(id, s);
	}

	// =========================
	// DELETE
	// =========================
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		scholarService.delete(id);
		return ResponseEntity.ok("Deleted successfully");
	}

	// =========================
	// GET SUPERVISOR
	// =========================
	@GetMapping("/{id}/supervisor")
	public List<ScholarSupervisor> getSupervisor(@PathVariable Integer id) {
		return scholarService.getSupervisor(id);
	}

	// =========================
	// GET HOD
	// =========================
	@GetMapping("/{id}/hod")
	public List<ProgramRoleAssignment> getHod(@PathVariable Integer id, @PathVariable String role) {
		return scholarService.getProgramByRole(id, role);
	}
	
	@GetMapping("/search")
	public ResponseEntity<Page<ScholarDTO>> searchScholars(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String keyword,
	        @RequestParam(required = false) Integer deptId
	        
	) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<ScholarDTO> result = scholarService.search(pageable,keyword,deptId);

	    return ResponseEntity.ok(result);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
	    try {
	        scholarService.importScholars(file);
	        return ResponseEntity.ok("Uploaded successfully");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}
	
}
