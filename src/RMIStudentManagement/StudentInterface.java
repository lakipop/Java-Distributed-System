package RMIStudentManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * ============================================================
 * RMI STUDENT MANAGEMENT INTERFACE
 * ============================================================
 * Remote interface defining student management operations.
 * Supports CRUD operations for student records.
 * 
 * Operations:
 *   - addStudent: Register new student
 *   - getStudent: Retrieve student by ID
 *   - updateStudent: Modify student details
 *   - deleteStudent: Remove student record
 *   - getAllStudents: List all students
 * ============================================================
 */
public interface StudentInterface extends Remote {

    // ========== CREATE OPERATIONS ==========
    
    /**
     * Add a new student to the system
     * @param name Student's full name
     * @param course Student's course/program
     * @param gpa Student's GPA
     * @return Generated student ID
     */
    String addStudent(String name, String course, double gpa) throws RemoteException;

    // ========== READ OPERATIONS ==========
    
    /**
     * Get student details by ID
     * @param studentId Student's unique ID
     * @return Student information string
     */
    String getStudent(String studentId) throws RemoteException;
    
    /**
     * Get all students in the system
     * @return List of all student information strings
     */
    List<String> getAllStudents() throws RemoteException;
    
    /**
     * Search students by name (partial match)
     * @param searchName Name to search for
     * @return List of matching student information
     */
    List<String> searchStudents(String searchName) throws RemoteException;

    // ========== UPDATE OPERATIONS ==========
    
    /**
     * Update student's course
     * @param studentId Student ID
     * @param newCourse New course name
     * @return Success/error message
     */
    String updateCourse(String studentId, String newCourse) throws RemoteException;
    
    /**
     * Update student's GPA
     * @param studentId Student ID
     * @param newGpa New GPA value
     * @return Success/error message
     */
    String updateGpa(String studentId, double newGpa) throws RemoteException;

    // ========== DELETE OPERATIONS ==========
    
    /**
     * Delete a student from the system
     * @param studentId Student ID to delete
     * @return Success/error message
     */
    String deleteStudent(String studentId) throws RemoteException;

    // ========== STATISTICS ==========
    
    /**
     * Get total number of students
     * @return Count of all students
     */
    int getStudentCount() throws RemoteException;
    
    /**
     * Get average GPA of all students
     * @return Average GPA value
     */
    double getAverageGpa() throws RemoteException;
}
