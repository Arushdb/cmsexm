package edu.dei.examination.phd.dto;

public class ActiveSemesterDTO {

    private int semesterId;
	private String status;              // SUCCESS / FAILED
    private String semester;            // e.g. "Semester 7"
    private boolean registrationOpen;   // true / false
    private String message;
    private int scholarid;

    
	public ActiveSemesterDTO(int semesterId, String status, String semester, 
			boolean registrationOpen, String message,int schid) {
		
		this.semesterId = semesterId;
		this.status = status;
		this.semester = semester;
		this.registrationOpen = registrationOpen;
		this.message = message;
		this.scholarid = schid;
	
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public boolean isRegistrationOpen() {
        return registrationOpen;
    }

    public void setRegistrationOpen(boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public int getSemesterId() { return semesterId; }

	public void setSemesterId(int semesterId) { this.semesterId = semesterId; }

	public int getScholarid() { return scholarid; }

	public void setScholarid(int scholarid) { this.scholarid = scholarid; }
	
	
    
    
}
