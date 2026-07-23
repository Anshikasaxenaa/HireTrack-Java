package com.candidatetracker.model;

import java.util.List;

/**
 * Represents a Job Role that candidates can apply for.
 */
public class JobRole {
    private int id;
    private String title;
    private String department;
    private List<String> requiredSkills;
    private boolean isOpen;

    public JobRole(int id, String title, String department, List<String> requiredSkills, boolean isOpen) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.requiredSkills = requiredSkills;
        this.isOpen = isOpen;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean isOpen) { this.isOpen = isOpen; }

    @Override
    public String toString() {
        return "JobRole{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", isOpen=" + isOpen +
                '}';
    }
}
