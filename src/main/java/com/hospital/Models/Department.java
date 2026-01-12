package com.hospital.Models;

public class Department {
    private int departmentId;
    private String name;
    private String location;

    @Override
    public String toString() {
        return "Department{" + "departmentId=" + departmentId + ", name='" + name + '\'' + ", location='" + location + '\'' + '}';
    }
}