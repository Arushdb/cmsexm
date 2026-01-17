package edu.dei.examination.phd.dto;



public class CreateScholarsRequest {

    private String academicYear;
    private Integer admissionMonth;

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getAdmissionMonth() {
        return admissionMonth;
    }

    public void setAdmissionMonth(Integer admissionMonth) {
        this.admissionMonth = admissionMonth;
    }
}
