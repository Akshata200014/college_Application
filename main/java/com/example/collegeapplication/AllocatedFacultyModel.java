package com.example.collegeapplication;
public class AllocatedFacultyModel {
    private String facultyName;
    private String subject;

    public AllocatedFacultyModel(String facultyName, String subject) {
        this.facultyName = facultyName;
        this.subject = subject;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getSubject() {
        return subject;
    }
}
