package com.candidatetracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a job candidate in the tracker.
 */
public class Candidate {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private List<String> skills;
    private int experienceYears;
    private String appliedRole;
    private Status status;
    private String dateAdded; // Kept as String for simplicity in file I/O

    public Candidate(int id, String fullName, String email, String phone, List<String> skills, 
                     int experienceYears, String appliedRole, Status status, String dateAdded) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
        this.experienceYears = experienceYears;
        this.appliedRole = appliedRole;
        this.status = status;
        this.dateAdded = dateAdded;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getAppliedRole() { return appliedRole; }
    public void setAppliedRole(String appliedRole) { this.appliedRole = appliedRole; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    /**
     * Converts Candidate to a CSV formatted string for saving to a file.
     * Skills are joined by a pipe '|' to avoid conflict with the comma separator.
     */
    public String toCSV() {
        String skillsStr = String.join("|", skills);
        return String.join(",", 
            String.valueOf(id), 
            fullName, 
            email, 
            phone, 
            skillsStr, 
            String.valueOf(experienceYears), 
            appliedRole, 
            status.name(), 
            dateAdded
        );
    }

    /**
     * Creates a Candidate object from a CSV formatted string.
     */
    public static Candidate fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        
        int id = Integer.parseInt(parts[0]);
        String fullName = parts[1];
        String email = parts[2];
        String phone = parts[3];
        
        List<String> skills = new ArrayList<>();
        if (!parts[4].isEmpty()) {
            for (String skill : parts[4].split("\\|")) {
                skills.add(skill);
            }
        }
        
        int experienceYears = Integer.parseInt(parts[5]);
        String appliedRole = parts[6];
        Status status = Status.valueOf(parts[7]);
        String dateAdded = parts[8];

        return new Candidate(id, fullName, email, phone, skills, experienceYears, appliedRole, status, dateAdded);
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Name: %-20s | Role: %-15s | Status: %-10s | Exp: %d yrs | Skills: %s | Added: %s",
                id, fullName, appliedRole, status, experienceYears, skills, dateAdded);
    }
}
