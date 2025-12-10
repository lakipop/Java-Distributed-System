package RMIStudentManagement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 * RMI STUDENT MANAGEMENT CLIENT
 * ============================================================
 * Client application for connecting to Student Management service.
 * Provides menu-driven interface for CRUD operations.
 * 
 * Features:
 *   - Add new students
 *   - View student details
 *   - Update student information
 *   - Delete students
 *   - View statistics
 * ============================================================
 */
public class StudentClient {

    // ========== CLIENT CONFIGURATION ==========
    private static final String SERVER_URL = "rmi://localhost:1101/StudentService";
    
    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RMI STUDENT MANAGEMENT CLIENT");
        System.out.println("============================================");
        
        try {
            // ========== CONNECT TO RMI SERVICE ==========
            StudentInterface studentService = (StudentInterface) Naming.lookup(SERVER_URL);
            System.out.println("Connected to Student Management Service!");
            
            Scanner scanner = new Scanner(System.in);
            
            // ========== MAIN MENU LOOP ==========
            while (true) {
                displayMenu();
                System.out.print("Choose option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        // ========== ADD STUDENT ==========
                        System.out.print("Enter student name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter course: ");
                        String course = scanner.nextLine();
                        System.out.print("Enter GPA (0-4.0): ");
                        double gpa = scanner.nextDouble();
                        
                        String studentId = studentService.addStudent(name, course, gpa);
                        System.out.println("Student added! ID: " + studentId);
                        break;
                        
                    case 2:
                        // ========== VIEW STUDENT ==========
                        System.out.print("Enter student ID: ");
                        String viewId = scanner.nextLine();
                        
                        String studentInfo = studentService.getStudent(viewId);
                        System.out.println(studentInfo);
                        break;
                        
                    case 3:
                        // ========== VIEW ALL STUDENTS ==========
                        List<String> allStudents = studentService.getAllStudents();
                        System.out.println("\n--- All Students ---");
                        if (allStudents.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            for (String student : allStudents) {
                                System.out.println(student);
                            }
                        }
                        break;
                        
                    case 4:
                        // ========== SEARCH STUDENTS ==========
                        System.out.print("Enter name to search: ");
                        String searchName = scanner.nextLine();
                        
                        List<String> results = studentService.searchStudents(searchName);
                        System.out.println("\n--- Search Results ---");
                        if (results.isEmpty()) {
                            System.out.println("No matching students found.");
                        } else {
                            for (String result : results) {
                                System.out.println(result);
                            }
                        }
                        break;
                        
                    case 5:
                        // ========== UPDATE COURSE ==========
                        System.out.print("Enter student ID: ");
                        String courseId = scanner.nextLine();
                        System.out.print("Enter new course: ");
                        String newCourse = scanner.nextLine();
                        
                        String courseResult = studentService.updateCourse(courseId, newCourse);
                        System.out.println(courseResult);
                        break;
                        
                    case 6:
                        // ========== UPDATE GPA ==========
                        System.out.print("Enter student ID: ");
                        String gpaId = scanner.nextLine();
                        System.out.print("Enter new GPA: ");
                        double newGpa = scanner.nextDouble();
                        
                        String gpaResult = studentService.updateGpa(gpaId, newGpa);
                        System.out.println(gpaResult);
                        break;
                        
                    case 7:
                        // ========== DELETE STUDENT ==========
                        System.out.print("Enter student ID to delete: ");
                        String deleteId = scanner.nextLine();
                        
                        String deleteResult = studentService.deleteStudent(deleteId);
                        System.out.println(deleteResult);
                        break;
                        
                    case 8:
                        // ========== VIEW STATISTICS ==========
                        int count = studentService.getStudentCount();
                        double avgGpa = studentService.getAverageGpa();
                        
                        System.out.println("\n--- Statistics ---");
                        System.out.println("Total Students: " + count);
                        System.out.printf("Average GPA: %.2f%n", avgGpa);
                        break;
                        
                    case 9:
                        // ========== EXIT ==========
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                
                System.out.println();
            }
            
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== DISPLAY MENU ==========
    private static void displayMenu() {
        System.out.println("\n--- STUDENT MANAGEMENT MENU ---");
        System.out.println("1. Add Student");
        System.out.println("2. View Student");
        System.out.println("3. View All Students");
        System.out.println("4. Search Students");
        System.out.println("5. Update Course");
        System.out.println("6. Update GPA");
        System.out.println("7. Delete Student");
        System.out.println("8. View Statistics");
        System.out.println("9. Exit");
    }
}
