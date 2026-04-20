package edu.dei.examination.phd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.dei.examination.phd.dto.ApiResponse;
import edu.dei.examination.phd.dto.FacultyDTO;
import edu.dei.examination.phd.service.FacultyService;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public FacultyDTO create(@RequestParam String name) {
        return facultyService.createFaculty(name);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>>  getAll() {
        //facultyService.getAllFaculties();
        return ResponseEntity.ok(ApiResponse.success("Faculties fetched successfully", 
        		facultyService.getAllFaculties()));
              
        
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> search(@RequestParam String name) {
    	 return ResponseEntity.ok(ApiResponse.success("Faculties fetched successfully", 
         		facultyService.searchFaculty(name)));
       // return facultyService.searchFaculty(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        facultyService.deleteFaculty(id);
    }
}
