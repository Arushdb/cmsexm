package edu.dei.examination.phd.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.DepartmentDTO;
import edu.dei.examination.phd.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    // =========================
    // ✅ GET ALL
    // =========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Departments fetched successfully",
                        service.getAll(), null)
        );
    }

    // =========================
    // ✅ GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Department fetched",
                        service.getById(id), null)
        );
    }

    // =========================
    // ✅ GET BY FACULTY (IMPORTANT)
    // =========================
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getByFaculty(
            @PathVariable Integer facultyId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Departments fetched by faculty",
                        service.getByFaculty(facultyId), null)
        );
    }

    // =========================
    // ✅ CREATE
    // =========================
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody DepartmentDTO dto) {

        service.create(dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Department created successfully", null, null)
        );
    }

    // =========================
    // ✅ UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Integer id,
                                                 @RequestBody DepartmentDTO dto) {

        service.update(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Department updated successfully", null, null)
        );
    }

    // =========================
    // ✅ DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Integer id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Department deleted successfully", null, null)
        );
    }

    // =========================
    // ✅ SEARCH (OPTIONAL)
    // =========================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Departments found",
                        service.search(keyword), null)
        );
    }
}