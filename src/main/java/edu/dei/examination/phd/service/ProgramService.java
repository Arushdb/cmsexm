package edu.dei.examination.phd.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.phd.dto.ProgramDTO;
import edu.dei.examination.phd.model.Program;
import edu.dei.examination.phd.repository.ProgramRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository repo;

    // =========================
    // GET ALL
    // =========================
    public List<ProgramDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // GET BY ID
    // =========================
    public ProgramDTO getById(Integer id) {
        Program p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        return mapToDTO(p);
    }

    // =========================
    // CREATE
    // =========================
    public Program create(ProgramDTO dto) {

        Program p = new Program();
        p.setProgramname(dto.getName());
        p.setMode(dto.getMode());

        return repo.save(p);
    }

    // =========================
    // UPDATE
    // =========================
    public Program update(Integer id, ProgramDTO dto) {

        Program p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        p.setProgramname(dto.getName());
        p.setMode(dto.getMode());

        return repo.save(p);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    // =========================
    // MAPPER
    // =========================
    private ProgramDTO mapToDTO(Program p) {

        ProgramDTO dto = new ProgramDTO();
        dto.setProgramId(p.getProgramId().intValue());
        dto.setName(p.getProgramname());
        dto.setMode(p.getMode());

        return dto;
    }
}