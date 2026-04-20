package edu.dei.examination.phd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.ProgramDTO;
import edu.dei.examination.phd.service.ProgramService;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
@CrossOrigin
public class ProgramController {

    @Autowired
    private ProgramService service;

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgramDTO>>> getAll() {

        List<ProgramDTO> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Programs fetched successfully", list, null)
        );
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgramDTO>> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Program fetched", service.getById(id), null)
        );
    }

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody ProgramDTO dto) {

        service.create(dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Program created successfully", null, null)
        );
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(@PathVariable Integer id,
                                                 @RequestBody ProgramDTO dto) {

        service.update(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Program updated successfully", null, null)
        );
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Integer id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Program deleted successfully", null, null)
        );
    }
}