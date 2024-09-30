package edu.dei.examination.cmsexm.model;



public class TranscriptData {
    private String roll_number;
	private String sem;
    private String session;
    private String courseCodeName;
    private String finalGradePoint;
    private String credit;
    private String Sgpa;

    // Getters and setters for each field
    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getCourseCodeName() {
        return courseCodeName;
    }

    public void setCourseCodeName(String courseCodeName) {
        this.courseCodeName = courseCodeName;
    }

    public String getFinalGradePoint() {
        return finalGradePoint;
    }

    public void setFinalGradePoint(String finalGradePoint) {
        this.finalGradePoint = finalGradePoint;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

	public String getRoll_number() {
		return roll_number;
	}

	public void setRoll_number(String roll_number) {
		this.roll_number = roll_number;
	}
	
	public String getSgpa() {
        return Sgpa;
    }

    public void setSgpa(String Sgpa) {
        this.Sgpa = Sgpa;
    }

	
}



