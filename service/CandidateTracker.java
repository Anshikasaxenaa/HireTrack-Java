package com.candidatetracker.service;

import com.candidatetracker.model.Candidate;
import com.candidatetracker.model.Status;
import com.candidatetracker.util.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Service class that manages the core logic of tracking candidates.
 */
public class CandidateTracker {
    private List<Candidate> candidates;
    private FileManager fileManager;
    private String dataFilePath;
    private int nextId;
    private Queue<Candidate> interviewQueue;

    public CandidateTracker(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.fileManager = new FileManager();
        this.candidates = fileManager.loadCandidatesFromFile(dataFilePath);
        
        // Ensure candidates are sorted by ID so Binary Search works
        Collections.sort(this.candidates, new Comparator<Candidate>() {
            @Override
            public int compare(Candidate c1, Candidate c2) {
                return Integer.compare(c1.getId(), c2.getId());
            }
        });
        
        this.nextId = calculateNextId();
        
        // Initialize interview queue
        this.interviewQueue = new LinkedList<>();
        for (Candidate c : candidates) {
            if (c.getStatus() == Status.INTERVIEW) {
                interviewQueue.offer(c);
            }
        }
    }

    /**
     * Determines the next candidate ID by finding the max ID in the loaded data.
     */
    private int calculateNextId() {
        int maxId = 0;
        for (Candidate c : candidates) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Adds a new candidate to the tracker.
     */
    public void addCandidate(String fullName, String email, String phone, List<String> skills, 
                             int experienceYears, String appliedRole) {
        String dateAdded = LocalDate.now().toString(); // Use current date
        Candidate newCandidate = new Candidate(nextId++, fullName, email, phone, skills, 
                                               experienceYears, appliedRole, Status.APPLIED, dateAdded);
        candidates.add(newCandidate);
        System.out.println("Candidate added successfully with ID: " + newCandidate.getId());
    }

    /**
     * Displays all candidates, optionally sorted by experience.
     */
    public void viewAllCandidates(boolean sortByExperience) {
        if (candidates.isEmpty()) {
            System.out.println("No candidates to display.");
            return;
        }

        List<Candidate> displayList = new ArrayList<>(candidates);
        
        if (sortByExperience) {
            // Sort by experience years descending
            Collections.sort(displayList, new Comparator<Candidate>() {
                @Override
                public int compare(Candidate c1, Candidate c2) {
                    return Integer.compare(c2.getExperienceYears(), c1.getExperienceYears());
                }
            });
            System.out.println("\n--- All Candidates (Sorted by Experience) ---");
        } else {
            System.out.println("\n--- All Candidates ---");
        }

        for (Candidate c : displayList) {
            System.out.println(c);
        }
    }

    /**
     * Searches for candidates possessing a specific skill (case-insensitive).
     */
    public void searchBySkill(String skill) {
        boolean found = false;
        System.out.println("\n--- Search Results for Skill: " + skill + " ---");
        for (Candidate c : candidates) {
            for (String candidateSkill : c.getSkills()) {
                if (candidateSkill.equalsIgnoreCase(skill)) {
                    System.out.println(c);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("No candidates found with skill: " + skill);
        }
    }

    /**
     * Searches for candidates by their pipeline status.
     */
    public void searchByStatus(Status status) {
        boolean found = false;
        System.out.println("\n--- Search Results for Status: " + status + " ---");
        for (Candidate c : candidates) {
            if (c.getStatus() == status) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No candidates found in status: " + status);
        }
    }

    /**
     * Searches for candidates by applied job role (case-insensitive substring match).
     */
    public void viewCandidatesByRole(String role) {
        boolean found = false;
        System.out.println("\n--- Search Results for Role: " + role + " ---");
        for (Candidate c : candidates) {
            if (c.getAppliedRole().toLowerCase().contains(role.toLowerCase())) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No candidates found for role containing: " + role);
        }
    }

    /**
     * Updates the status of an existing candidate.
     */
    public void updateStatus(int id, Status newStatus) {
        Candidate candidate = findCandidateById(id);
        if (candidate != null) {
            Status oldStatus = candidate.getStatus();
            candidate.setStatus(newStatus);
            System.out.println("Candidate ID " + id + " status updated from " + oldStatus + " to " + newStatus);
            
            // Queue management for interviews
            if (newStatus == Status.INTERVIEW && oldStatus != Status.INTERVIEW) {
                interviewQueue.offer(candidate);
                System.out.println("Candidate added to the interview queue.");
            }
        } else {
            System.out.println("Candidate with ID " + id + " not found.");
        }
    }

    /**
     * Deletes a candidate from the tracker.
     */
    public void deleteCandidate(int id) {
        Candidate candidate = findCandidateById(id);
        if (candidate != null) {
            candidates.remove(candidate);
            System.out.println("Candidate ID " + id + " deleted successfully.");
        } else {
            System.out.println("Candidate with ID " + id + " not found.");
        }
    }

    /**
     * Prints a summary of how many candidates are in each status.
     */
    public void printPipelineSummary() {
        Map<Status, Integer> counts = new HashMap<>();
        
        // Initialize all statuses to 0
        for (Status s : Status.values()) {
            counts.put(s, 0);
        }

        // Count occurrences
        for (Candidate c : candidates) {
            counts.put(c.getStatus(), counts.get(c.getStatus()) + 1);
        }

        System.out.println("\n--- Pipeline Summary ---");
        for (Status s : Status.values()) {
            System.out.println(s + ": " + counts.get(s));
        }
    }

    /**
     * Retrieves the next candidate in the interview queue.
     */
    public Candidate getNextInterview() {
        return interviewQueue.poll();
    }

    /**
     * Helper method to find a candidate by ID using Binary Search (O(log N)).
     */
    private Candidate findCandidateById(int id) {
        int left = 0;
        int right = candidates.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Candidate midCandidate = candidates.get(mid);

            if (midCandidate.getId() == id) {
                return midCandidate;
            }

            if (midCandidate.getId() < id) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }
        return null; // Not found
    }

    /**
     * Saves all candidate data before shutting down.
     */
    public void shutdown() {
        fileManager.saveCandidatesToFile(dataFilePath, candidates);
    }
}
