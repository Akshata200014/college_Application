package com.example.collegeapplication;

import java.io.Serializable;

public class Student implements Serializable {

    private String rollNo;
    private String name;
    private boolean isAttended;

    public Student() {
        // Default constructor required for Firebase
    }

    public Student(String rollNo, String name) {
        this.rollNo = rollNo;
        this.name = name;
        this.isAttended = false; // Default attendance status
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttended() {
        return isAttended;
    }

    public void setAttended(boolean attended) {
        isAttended = attended;
    }
}
