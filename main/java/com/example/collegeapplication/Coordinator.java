package com.example.collegeapplication;

public class Coordinator {
    private String name;
    private String classAssigned;

    public Coordinator(String name, String classAssigned) {
        this.name = name;
        this.classAssigned = classAssigned;
    }

    public String getName() {
        return name;
    }

    public String getClassAssigned() {
        return classAssigned;
    }
}

