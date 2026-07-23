<h1 align="center">Candidate Tracker (ATS) 🚀</h1>

<p align="center">
  <strong>A lightweight, high-performance Applicant Tracking System built entirely in Core Java.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/DSA-Optimized-brightgreen?style=for-the-badge" />
  <img src="https://img.shields.io/badge/CLI-Interface-blue?style=for-the-badge" />
</p>

---

## 📌 Overview
Candidate Tracker is a fast, robust console-based Applicant Tracking System (ATS) designed to manage software engineering candidates through a hiring pipeline. 

Built strictly with **Core Java**, this project demonstrates strong foundational software engineering skills, including Object-Oriented Design, the Java Collections Framework, explicit Data Structures & Algorithms (DSA), and persistent File I/O, without relying on heavy external frameworks or relational databases.

## 🌟 Key Features
- **Full Candidate Lifecycle Management:** Add, update, view, and delete candidates.
- **Automated Interview Queue:** Uses a `Queue` data structure to seamlessly stream candidates waiting for interviews.
- **Fast Lookups:** Implements **Binary Search** (`O(log N)`) for instantaneous ID-based candidate retrieval.
- **Dynamic Sorting:** Sort candidates by experience using custom `Comparator` logic.
- **Pipeline Analytics:** Generates real-time hiring pipeline summaries using `HashMap` frequency counting.
- **Persistent Storage:** Automatically serializes and deserializes Java Objects to/from a CSV file state on application shutdown/startup.
- **Crash-Resistant:** Extensive Exception Handling (`try-catch`) to manage file access errors and invalid user inputs gracefully.

---

## 🧠 Data Structures & Algorithms (DSA) Highlights
This project was specifically designed to demonstrate practical applications of DSA in a real-world system:

1. **Binary Search (`O(log N)`):** Replaced standard linear search with Binary Search for retrieving candidates by their auto-incrementing ID. This drastically reduces time complexity when updating or deleting candidates in large datasets.
2. **Queue (`First-In-First-Out`):** Implemented a `LinkedList`-backed `Queue` to manage the interview pipeline. Candidates moved to the `INTERVIEW` stage are enqueued. The "Process Next Interview" feature `poll()`s the queue to process candidates in the exact order they reached the interview stage.
3. **Hash Maps (`O(1)` access):** Utilized a `HashMap<Status, Integer>` to perform rapid frequency counting of candidates across all pipeline stages for the Analytics Summary.

---

## 🛠️ Architecture & Core Concepts
- **Object-Oriented Programming (OOP):** Encapsulation of domain entities (`Candidate`, `JobRole`) and use of `Enum` types (`Status`) for type-safe pipeline stages.
- **Separation of Concerns:**
  - `model/`: Contains pure data entities.
  - `service/`: Contains `CandidateTracker` which handles all business logic, search algorithms, and queue management.
  - `util/`: Contains `FileManager` for isolated File I/O (`BufferedReader`/`BufferedWriter`).
  - `Main.java`: Exclusively handles the interactive CLI layer and `Scanner` I/O.

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher.

### Installation & Execution
1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/candidate-tracker.git
   cd candidate-tracker
   ```
2. **Compile the source code**
   ```bash
   javac -d out src/com/candidatetracker/model/*.java src/com/candidatetracker/util/*.java src/com/candidatetracker/service/*.java src/com/candidatetracker/Main.java
   ```
3. **Run the application**
   ```bash
   java -cp out com.candidatetracker.Main
   ```

*(Note: The system will automatically generate a `candidates.csv` file on first shutdown to store data, or you can use the provided seeded CSV).*

---

## 💻 Menu Interface Preview

```text
==========================================
  Welcome to the Candidate Tracker ATS!   
==========================================

----------- MAIN MENU -----------
1. Add Candidate
2. View All Candidates
3. Search by Skill
4. Search by Status
5. Update Candidate Status
6. Delete Candidate
7. View Candidates by Job Role
8. View Pipeline Summary
9. Process Next Interview (Queue)
10. Exit
---------------------------------

Enter your choice (1-10): 
```
