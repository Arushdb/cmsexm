package edu.dei.examination.phd.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.phd.dto.DepartmentDTO;
import edu.dei.examination.phd.model.Department;
import edu.dei.examination.phd.model.Faculty;
import edu.dei.examination.phd.repository.DepartmentRepository;
import edu.dei.examination.phd.repository.FacultyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

	@Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private FacultyRepository facultyRepo;

    // =========================
    // ✅ GET ALL DEPARTMENTS
    // =========================
    public List<DepartmentDTO> getAll() {
        return departmentRepo.findAllAsDTO();
//        		findAll()
//                .stream()
//                .map(this::mapToDTO)
//                .toList();
                
    }

    // =========================
    // ✅ GET BY FACULTY
    // =========================
    public List<DepartmentDTO> getByFaculty(Integer facultyId) {

        return departmentRepo.findByFaculty_Id(facultyId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // ✅ GET BY ID
    // =========================
    public DepartmentDTO getById(Integer id) {

        Department d = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return mapToDTO(d);
    }

    // =========================
    // ✅ CREATE
    // =========================
    public Department create(DepartmentDTO dto) {

        Faculty faculty = facultyRepo.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Department d = new Department();
        d.setDepartmentName(dto.getName());
        d.setDepartmentCode(dto.getCode());
        d.setFaculty(faculty);

        return departmentRepo.save(d);
    }

    // =========================
    // ✅ UPDATE
    // =========================
    public Department update(Integer id, DepartmentDTO dto) {

        Department d = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        d.setDepartmentName(dto.getName());
        d.setDepartmentCode(dto.getCode());

        if (dto.getFacultyId() != null) {
            Faculty faculty = facultyRepo.findById(dto.getFacultyId())
                    .orElseThrow(() -> new RuntimeException("Faculty not found"));
            d.setFaculty(faculty);
        }

        return departmentRepo.save(d);
    }

    // =========================
    // ✅ DELETE
    // =========================
    public void delete(Integer id) {

        if (!departmentRepo.existsById(id)) {
            throw new RuntimeException("Department not found");
        }

        departmentRepo.deleteById(id);
    }

    // =========================
    // ✅ SEARCH (OPTIONAL)
    // =========================
    public List<DepartmentDTO> search(String keyword) {

        return departmentRepo.searchByName(keyword)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 ENTITY → DTO MAPPING
    // =========================
    private DepartmentDTO mapToDTO(Department d) {

        DepartmentDTO dto = new DepartmentDTO();

        dto.setId(d.getDepartmentId().intValue());
        dto.setName(d.getDepartmentName());
        dto.setCode(d.getDepartmentCode());

        if (d.getFaculty() != null) {
            dto.setFacultyId(d.getFaculty().getId());
            
            dto.setFacultyName(d.getFaculty().getName());
        }

        return dto;
    }
}