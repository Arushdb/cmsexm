package edu.dei.examination.phd.dto;



public class FacultyDTO {

    private Integer id;
    private String facultyName;

    public FacultyDTO(Integer id, String facultyName) {
        this.id = id;
        this.facultyName = facultyName;
    }

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getFacultyName() { return facultyName; }

	public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    // Getters & Setters
}