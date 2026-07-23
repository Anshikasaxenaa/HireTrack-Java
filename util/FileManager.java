package com.candidatetracker.util;

import com.candidatetracker.model.Candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file Input/Output operations for the application.
 */
public class FileManager {
    
    /**
     * Reads candidates from a specified CSV file.
     * @param path The file path
     * @return A list of Candidate objects
     */
    public List<Candidate> loadCandidatesFromFile(String path) {
        List<Candidate> candidates = new ArrayList<>();
        File file = new File(path);
        
        // Return empty list if file doesn't exist yet
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting with an empty tracker.");
            return candidates;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                
                try {
                    Candidate candidate = Candidate.fromCSV(line);
                    candidates.add(candidate);
                } catch (Exception e) {
                    System.out.println("Warning: Skipping malformed line in file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the candidates file: " + e.getMessage());
        }
        
        return candidates;
    }

    /**
     * Writes the current list of candidates back to the CSV file.
     * @param path The file path
     * @param candidates The list of candidates to save
     */
    public void saveCandidatesToFile(String path, List<Candidate> candidates) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            // Write the header
            bw.write("ID,FullName,Email,Phone,Skills,ExperienceYears,AppliedRole,Status,DateAdded\n");
            
            // Write each candidate
            for (Candidate candidate : candidates) {
                bw.write(candidate.toCSV() + "\n");
            }
            System.out.println("Successfully saved data to " + path);
        } catch (IOException e) {
            System.out.println("Error saving candidates to file: " + e.getMessage());
        }
    }
}
