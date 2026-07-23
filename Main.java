package com.candidatetracker;

import com.candidatetracker.model.Status;
import com.candidatetracker.service.CandidateTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Candidate Tracker application.
 * Handles user input and menu navigation.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dataFilePath = "candidates.csv";
        CandidateTracker tracker = new CandidateTracker(dataFilePath);
        
        System.out.println("==========================================");
        System.out.println("  Welcome to the Candidate Tracker ATS!   ");
        System.out.println("==========================================");

        boolean running = true;
        
        while (running) {
            printMenu();
            System.out.print("\nEnter your choice (1-10): ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        addCandidateMenu(scanner, tracker);
                        break;
                    case "2":
                        viewAllMenu(scanner, tracker);
                        break;
                    case "3":
                        System.out.print("Enter skill to search for: ");
                        String skill = scanner.nextLine().trim();
                        tracker.searchBySkill(skill);
                        break;
                    case "4":
                        searchByStatusMenu(scanner, tracker);
                        break;
                    case "5":
                        updateStatusMenu(scanner, tracker);
                        break;
                    case "6":
                        deleteCandidateMenu(scanner, tracker);
                        break;
                    case "7":
                        System.out.print("Enter job role to search for: ");
                        String role = scanner.nextLine().trim();
                        tracker.viewCandidatesByRole(role);
                        break;
                    case "8":
                        tracker.printPipelineSummary();
                        break;
                    case "9":
                        processNextInterviewMenu(scanner, tracker);
                        break;
                    case "10":
                        System.out.println("Saving data and shutting down...");
                        tracker.shutdown();
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try your operation again.");
            }
        }
        
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println("1. Add Candidate");
        System.out.println("2. View All Candidates");
        System.out.println("3. Search by Skill");
        System.out.println("4. Search by Status");
        System.out.println("5. Update Candidate Status");
        System.out.println("6. Delete Candidate");
        System.out.println("7. View Candidates by Job Role");
        System.out.println("8. View Pipeline Summary");
        System.out.println("9. Process Next Interview (Queue)");
        System.out.println("10. Exit");
        System.out.println("---------------------------------");
    }

    private static void addCandidateMenu(Scanner scanner, CandidateTracker tracker) {
        System.out.println("\n--- Add New Candidate ---");
        
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Aborting.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Skills (comma separated, e.g., Java, SQL, Spring): ");
        String skillsStr = scanner.nextLine().trim();
        List<String> skills = new ArrayList<>();
        if (!skillsStr.isEmpty()) {
            for (String s : skillsStr.split(",")) {
                skills.add(s.trim());
            }
        }

        System.out.print("Experience (Years as an integer): ");
        int expYears = 0;
        try {
            expYears = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Setting experience to 0.");
        }

        System.out.print("Applied Role: ");
        String role = scanner.nextLine().trim();

        tracker.addCandidate(name, email, phone, skills, expYears, role);
    }

    private static void viewAllMenu(Scanner scanner, CandidateTracker tracker) {
        System.out.print("Sort by experience? (y/n): ");
        String sortChoice = scanner.nextLine().trim().toLowerCase();
        boolean sort = sortChoice.equals("y") || sortChoice.equals("yes");
        tracker.viewAllCandidates(sort);
    }

    private static void searchByStatusMenu(Scanner scanner, CandidateTracker tracker) {
        System.out.println("Available Statuses: " + Arrays.toString(Status.values()));
        System.out.print("Enter status: ");
        String statusStr = scanner.nextLine().trim().toUpperCase();
        
        try {
            Status status = Status.valueOf(statusStr);
            tracker.searchByStatus(status);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter one of the exact statuses listed above.");
        }
    }

    private static void updateStatusMenu(Scanner scanner, CandidateTracker tracker) {
        System.out.print("Enter Candidate ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.println("Available Statuses: " + Arrays.toString(Status.values()));
            System.out.print("Enter new status: ");
            String statusStr = scanner.nextLine().trim().toUpperCase();
            
            Status status = Status.valueOf(statusStr);
            tracker.updateStatus(id, status);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter one of the exact statuses listed above.");
        }
    }

    private static void deleteCandidateMenu(Scanner scanner, CandidateTracker tracker) {
        System.out.print("Enter Candidate ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            tracker.deleteCandidate(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }

    private static void processNextInterviewMenu(Scanner scanner, CandidateTracker tracker) {
        com.candidatetracker.model.Candidate c = tracker.getNextInterview();
        if (c == null) {
            System.out.println("The interview queue is currently empty.");
        } else {
            System.out.println("\n--- Processing Interview ---");
            System.out.println("Candidate: " + c.getFullName() + " (ID: " + c.getId() + ")");
            System.out.print("Did the candidate pass the interview? (y/n): ");
            String passChoice = scanner.nextLine().trim().toLowerCase();
            if (passChoice.equals("y") || passChoice.equals("yes")) {
                tracker.updateStatus(c.getId(), Status.OFFER);
            } else {
                tracker.updateStatus(c.getId(), Status.REJECTED);
            }
        }
    }
}
