package edu.dei.examination.phd.dto;



public class DepartmentDTO {

    private Integer id;
    private String name;
    

    // =========================
    // FACULTY INFO
    // =========================
    private Integer facultyId;
    private String facultyName;
    
    // =========================
    // OPTIONAL
    // =========================
    private String code;

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public DepartmentDTO(Integer id, String name, Integer facultyId, String facultyName) {
		
		this.id = id;
		this.name = name;
		this.facultyId = facultyId;
		this.facultyName = facultyName;
	}

	public DepartmentDTO() {
		
		// TODO Auto-generated constructor stub
	}
    
    
}