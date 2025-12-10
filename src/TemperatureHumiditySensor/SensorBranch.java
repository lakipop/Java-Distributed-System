package TemperatureHumiditySensor;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ============================================================
 * SENSOR BRANCH - REMOTE SENSOR NODE (SUB-BRANCH)
 * ============================================================
 * Remote sensor that connects to the Base Station to report
 * temperature and humidity readings. Each sensor has a unique ID.
 * 
 * Architecture:
 *   - Sensor Branch (this class) acts as a sub-branch/client
 *   - Connects to Base Station (central hub)
 *   - Sends sensor ID along with temperature and humidity
 *   - Receives calculated status from Base Station
 * 
 * Features:
 *   - Unique sensor ID identification
 *   - Random or manual temperature/humidity generation
 *   - Periodic automatic readings (optional)
 *   - Real-time status feedback from base station
 * ============================================================
 */
public class SensorBranch {

    // ========== CONNECTION CONFIGURATION ==========
    private static final String BASE_STATION_HOST = "localhost";
    private static final int BASE_STATION_PORT = 5004;
    
    // ========== SENSOR IDENTIFICATION ==========
    private static String sensorId;
    
    // ========== RANDOM GENERATOR FOR SENSOR READINGS ==========
    private static final Random random = new Random();

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   TEMPERATURE-HUMIDITY SENSOR BRANCH (SUB-BRANCH)");
        System.out.println("============================================================");
        
        Scanner scanner = new Scanner(System.in);
        
        // ========== GET SENSOR ID FROM USER ==========
        System.out.print("Enter Sensor ID (e.g., SENSOR-001): ");
        sensorId = scanner.nextLine().trim();
        
        if (sensorId.isEmpty()) {
            // Generate random ID if not provided
            sensorId = "SENSOR-" + String.format("%03d", random.nextInt(1000));
            System.out.println("Generated Sensor ID: " + sensorId);
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.println("Sensor " + sensorId + " initialized.");
        System.out.println("------------------------------------------------------------");
        
        // ========== MAIN MENU LOOP ==========
        while (true) {
            displayMenu();
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    // ========== SEND RANDOM READING ==========
                    sendRandomReading();
                    break;
                    
                case 2:
                    // ========== SEND MANUAL READING ==========
                    System.out.print("Enter temperature (°C): ");
                    double temp = scanner.nextDouble();
                    System.out.print("Enter humidity (%): ");
                    double humidity = scanner.nextDouble();
                    sendReading(temp, humidity);
                    break;
                    
                case 3:
                    // ========== START AUTOMATIC READINGS ==========
                    System.out.print("Enter interval in seconds: ");
                    int interval = scanner.nextInt();
                    startAutomaticReadings(interval);
                    break;
                    
                case 4:
                    // ========== SIMULATE DIFFERENT CONDITIONS ==========
                    simulateConditions(scanner);
                    break;
                    
                case 5:
                    // ========== EXIT ==========
                    System.out.println("Sensor " + sensorId + " shutting down...");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Invalid option!");
            }
            
            System.out.println();
        }
    }

    // ========================================================
    // SEND READING TO BASE STATION
    // ========================================================
    /**
     * Sends temperature and humidity reading to the base station.
     * Receives and displays the calculated status.
     * 
     * @param temperature Temperature reading in Celsius
     * @param humidity Humidity reading in percentage
     */
    private static void sendReading(double temperature, double humidity) {
        try (
            Socket socket = new Socket(BASE_STATION_HOST, BASE_STATION_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // ========== CREATE SENSOR DATA ==========
            // Package sensor ID with readings
            SensorData sensorData = new SensorData(sensorId, temperature, humidity);
            
            System.out.println("\n[SENDING] Temperature: " + temperature + "°C, Humidity: " + humidity + "%");
            
            // ========== SEND TO BASE STATION ==========
            out.writeObject(sensorData);
            out.flush();
            
            // ========== RECEIVE RESPONSE ==========
            // Base station returns data with calculated status
            SensorData response = (SensorData) in.readObject();
            
            // ========== DISPLAY RESPONSE ==========
            System.out.println("------------------------------------------------------------");
            System.out.println("[RESPONSE FROM BASE STATION]");
            System.out.println("   Sensor ID:    " + response.getSensorId());
            System.out.println("   Temperature:  " + response.getTemperature() + "°C");
            System.out.println("   Humidity:     " + response.getHumidity() + "%");
            System.out.println("   Status:       " + getStatusDisplay(response.getStatus()));
            System.out.println("------------------------------------------------------------");
            
            // Show alert message for non-normal status
            if (!response.getStatus().equals(StatusCalculator.STATUS_NORMAL)) {
                String message = StatusCalculator.getStatusMessage(
                    response.getStatus(), 
                    response.getTemperature(), 
                    response.getHumidity()
                );
                System.out.println("   " + message);
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] Could not connect to Base Station: " + e.getMessage());
            System.err.println("Make sure Base Station is running on " + BASE_STATION_HOST + ":" + BASE_STATION_PORT);
        }
    }

    // ========================================================
    // RANDOM READING GENERATOR
    // ========================================================
    /**
     * Generates and sends random temperature and humidity readings.
     * Temperature range: 10°C to 40°C
     * Humidity range: 20% to 85%
     */
    private static void sendRandomReading() {
        // Generate random temperature (10°C to 40°C)
        double temperature = 10 + (random.nextDouble() * 30);
        temperature = Math.round(temperature * 10.0) / 10.0; // Round to 1 decimal
        
        // Generate random humidity (20% to 85%)
        double humidity = 20 + (random.nextDouble() * 65);
        humidity = Math.round(humidity * 10.0) / 10.0; // Round to 1 decimal
        
        sendReading(temperature, humidity);
    }

    // ========================================================
    // AUTOMATIC READING SCHEDULER
    // ========================================================
    /**
     * Starts automatic periodic readings at specified interval.
     * 
     * @param intervalSeconds Interval between readings in seconds
     */
    private static void startAutomaticReadings(int intervalSeconds) {
        System.out.println("Starting automatic readings every " + intervalSeconds + " seconds...");
        System.out.println("Press Ctrl+C to stop.");
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendRandomReading();
            }
        }, 0, intervalSeconds * 1000L);
        
        // Keep running for 5 readings then stop
        try {
            Thread.sleep(intervalSeconds * 5000L);
            timer.cancel();
            System.out.println("Automatic readings stopped after 5 cycles.");
        } catch (InterruptedException e) {
            timer.cancel();
        }
    }

    // ========================================================
    // CONDITION SIMULATOR
    // ========================================================
    /**
     * Simulates different environmental conditions for testing.
     */
    private static void simulateConditions(Scanner scanner) {
        System.out.println("\n--- Simulate Conditions ---");
        System.out.println("1. Normal conditions");
        System.out.println("2. High temperature (hot day)");
        System.out.println("3. Low temperature (cold environment)");
        System.out.println("4. High humidity (rainy/humid)");
        System.out.println("5. Low humidity (dry/desert)");
        System.out.println("6. Critical conditions");
        System.out.print("Choose condition: ");
        
        int condition = scanner.nextInt();
        
        double temp, humidity;
        
        switch (condition) {
            case 1: // Normal
                temp = 22 + random.nextDouble() * 6;
                humidity = 45 + random.nextDouble() * 15;
                break;
            case 2: // High temperature
                temp = 36 + random.nextDouble() * 8;
                humidity = 40 + random.nextDouble() * 20;
                break;
            case 3: // Low temperature
                temp = 5 + random.nextDouble() * 8;
                humidity = 50 + random.nextDouble() * 20;
                break;
            case 4: // High humidity
                temp = 25 + random.nextDouble() * 5;
                humidity = 75 + random.nextDouble() * 18;
                break;
            case 5: // Low humidity
                temp = 28 + random.nextDouble() * 5;
                humidity = 15 + random.nextDouble() * 10;
                break;
            case 6: // Critical
                temp = random.nextBoolean() ? -5 + random.nextDouble() * 3 : 46 + random.nextDouble() * 5;
                humidity = random.nextBoolean() ? 5 + random.nextDouble() * 4 : 92 + random.nextDouble() * 5;
                break;
            default:
                System.out.println("Invalid condition!");
                return;
        }
        
        sendReading(Math.round(temp * 10.0) / 10.0, Math.round(humidity * 10.0) / 10.0);
    }

    // ========================================================
    // HELPER METHODS
    // ========================================================
    
    /**
     * Display menu options
     */
    private static void displayMenu() {
        System.out.println("\n--- SENSOR MENU ---");
        System.out.println("1. Send Random Reading");
        System.out.println("2. Send Manual Reading");
        System.out.println("3. Start Automatic Readings");
        System.out.println("4. Simulate Conditions");
        System.out.println("5. Exit");
    }

    /**
     * Get formatted status display with icon
     */
    private static String getStatusDisplay(String status) {
        switch (status) {
            case "NORMAL":
                return "✅ NORMAL";
            case "WARNING":
                return "⚡ WARNING";
            case "CRITICAL":
                return "⚠️ CRITICAL";
            default:
                return status;
        }
    }
}
