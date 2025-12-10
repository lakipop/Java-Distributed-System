package RMIStudentManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * ============================================================
 * RMI STUDENT MANAGEMENT IMPLEMENTATION
 * ============================================================
 * Implementation of the Student remote interface.
 * Manages student records using HashMaps for storage.
 * 
 * Features:
 *   - CRUD operations for students
 *   - Search functionality
 *   - Statistics (count, average GPA)
 *   - Unique ID generation
 * ============================================================
 */
public class StudentImplementation extends UnicastRemoteObject implements StudentInterface {

    // ========== STUDENT DATA STORAGE ==========
    // HashMap storing student names
    private HashMap<String, String> studentNames = new HashMap<>();
    
    // HashMap storing student courses
    private HashMap<String, String> studentCourses = new HashMap<>();
    
    // HashMap storing student GPAs
    private HashMap<String, Double> studentGpas = new HashMap<>();
    
    // Counter for generating unique student IDs
    private AtomicInteger idCounter = new AtomicInteger(2024000);

    // ========== CONSTRUCTOR ==========
    public StudentImplementation() throws RemoteException {
        super();
        
        // Initialize with sample data
        initializeSampleData();
        
        System.out.println("Student Management Service initialized.");
    }

    // ========== SAMPLE DATA INITIALIZATION ==========
    private void initializeSampleData() {
        // Add some sample students
        try {
            addStudent("Lakindu Perera", "Computer Science", 3.75);
            addStudent("Nethmi Silva", "Software Engineering", 3.85);
            addStudent("Kasun Fernando", "Information Technology", 3.50);
        } catch (RemoteException e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }

    // ========== CREATE OPERATIONS ==========
    
    @Override
    public synchronized String addStudent(String name, String course, double gpa) throws RemoteException {
        // Validate GPA
        if (gpa < 0 || gpa > 4.0) {
            throw new RemoteException("GPA must be between 0 and 4.0");
        }
        
        // Generate unique student ID
        String studentId = "STU" + idCounter.incrementAndGet();
        
        // Store student data
        studentNames.put(studentId, name);
        studentCourses.put(studentId, course);
        studentGpas.put(studentId, gpa);
        
        System.out.println("[ADD] Student " + studentId + " - " + name + " added successfully.");
        return studentId;
    }

    // ========== READ OPERATIONS ==========
    
    @Override
    public String getStudent(String studentId) throws RemoteException {
        if (!studentNames.containsKey(studentId)) {
            return "Error: Student " + studentId + " not found.";
        }
        
        return formatStudentInfo(studentId);
    }
    
    @Override
    public List<String> getAllStudents() throws RemoteException {
        List<String> allStudents = new ArrayList<>();
        
        for (String studentId : studentNames.keySet()) {
            allStudents.add(formatStudentInfo(studentId));
        }
        
        return allStudents;
    }
    
    @Override
    public List<String> searchStudents(String searchName) throws RemoteException {
        return studentNames.entrySet().stream()
            .filter(entry -> entry.getValue().toLowerCase().contains(searchName.toLowerCase()))
            .map(entry -> formatStudentInfo(entry.getKey()))
            .collect(Collectors.toList());
    }

    // ========== UPDATE OPERATIONS ==========
    
    @Override
    public synchronized String updateCourse(String studentId, String newCourse) throws RemoteException {
        if (!studentNames.containsKey(studentId)) {
            return "Error: Student " + studentId + " not found.";
        }
        
        String oldCourse = studentCourses.get(studentId);
        studentCourses.put(studentId, newCourse);
        
        System.out.println("[UPDATE] " + studentId + " course changed from " + oldCourse + " to " + newCourse);
        return "Course updated successfully from " + oldCourse + " to " + newCourse;
    }
    
    @Override
    public synchronized String updateGpa(String studentId, double newGpa) throws RemoteException {
        if (!studentNames.containsKey(studentId)) {
            return "Error: Student " + studentId + " not found.";
        }
        
        if (newGpa < 0 || newGpa > 4.0) {
            return "Error: GPA must be between 0 and 4.0";
        }
        
        double oldGpa = studentGpas.get(studentId);
        studentGpas.put(studentId, newGpa);
        
        System.out.println("[UPDATE] " + studentId + " GPA changed from " + oldGpa + " to " + newGpa);
        return String.format("GPA updated from %.2f to %.2f", oldGpa, newGpa);
    }

    // ========== DELETE OPERATIONS ==========
    
    @Override
    public synchronized String deleteStudent(String studentId) throws RemoteException {
        if (!studentNames.containsKey(studentId)) {
            return "Error: Student " + studentId + " not found.";
        }
        
        String name = studentNames.remove(studentId);
        studentCourses.remove(studentId);
        studentGpas.remove(studentId);
        
        System.out.println("[DELETE] Student " + studentId + " - " + name + " deleted.");
        return "Student " + name + " (" + studentId + ") deleted successfully.";
    }

    // ========== STATISTICS ==========
    
    @Override
    public int getStudentCount() throws RemoteException {
        return studentNames.size();
    }
    
    @Override
    public double getAverageGpa() throws RemoteException {
        if (studentGpas.isEmpty()) {
            return 0.0;
        }
        
        double sum = studentGpas.values().stream().mapToDouble(Double::doubleValue).sum();
        return sum / studentGpas.size();
    }

    // ========== HELPER METHODS ==========
    
    private String formatStudentInfo(String studentId) {
        return String.format("ID: %s | Name: %s | Course: %s | GPA: %.2f",
            studentId,
            studentNames.get(studentId),
            studentCourses.get(studentId),
            studentGpas.get(studentId)
        );
    }
}
