package edu.dei.examination.phd.service;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.phd.dto.FacultyDTO;
import edu.dei.examination.phd.model.Faculty;
import edu.dei.examination.phd.repository.FacultyRepository;


@Service
public class FacultyService {

	@Autowired
	private  FacultyRepository facultyRepository ;

      
    public FacultyDTO createFaculty(String name) {

        if (facultyRepository.existsByName(name)) {
            throw new RuntimeException("Faculty already exists");
        }

        Faculty faculty = new Faculty();
        faculty.setName(name);

        Faculty saved = facultyRepository.save(faculty);

        return new FacultyDTO(saved.getId(), saved.getName());
    }

    
    public List<FacultyDTO> getAllFaculties() {
        return facultyRepository.findAllAsDTO();
    }

    
    public List<FacultyDTO> searchFaculty(String name) {
        return facultyRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(f -> new FacultyDTO(f.getId(), f.getName()))
                .collect(Collectors.toList());
    }

    
    public void deleteFaculty(Integer id) {
        facultyRepository.deleteById(id);
    }
}