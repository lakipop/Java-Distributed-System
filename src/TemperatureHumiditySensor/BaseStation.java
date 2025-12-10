package TemperatureHumiditySensor;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ============================================================
 * BASE STATION - CENTRAL MONITORING HUB
 * ============================================================
 * Central server that receives and processes data from multiple
 * sensor branches. Manages dynamic HashMaps for storing sensor
 * readings and calculating status.
 * 
 * Architecture:
 *   - Base Station (this class) acts as the central hub
 *   - Multiple Sensor Branches connect as clients
 *   - Each sensor sends its ID and receives temp, humidity, status
 * 
 * Features:
 *   - Dynamic HashMaps for temperature, humidity, and status
 *   - Automatic status calculation based on sensor readings
 *   - Thread-safe concurrent data management
 *   - Real-time monitoring dashboard
 * ============================================================
 */
public class BaseStation {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 5004;
    
    // ========================================================
    // DYNAMIC HASHMAPS FOR SENSOR DATA STORAGE
    // ========================================================
    // These HashMaps dynamically update when new sensors connect
    // or existing sensors send new readings.
    // ========================================================
    
    /**
     * HashMap storing temperature readings for each sensor
     * Key: Sensor ID (e.g., "SENSOR-001")
     * Value: Temperature in Celsius
     */
    private static ConcurrentHashMap<String, Double> temperatureMap = new ConcurrentHashMap<>();
    
    /**
     * HashMap storing humidity readings for each sensor
     * Key: Sensor ID (e.g., "SENSOR-001")
     * Value: Humidity percentage (0-100)
     */
    private static ConcurrentHashMap<String, Double> humidityMap = new ConcurrentHashMap<>();
    
    /**
     * HashMap storing calculated status for each sensor
     * Key: Sensor ID (e.g., "SENSOR-001")
     * Value: Status string (NORMAL, WARNING, CRITICAL)
     */
    private static ConcurrentHashMap<String, String> statusMap = new ConcurrentHashMap<>();
    
    /**
     * HashMap storing last update timestamp for each sensor
     * Key: Sensor ID
     * Value: Timestamp in milliseconds
     */
    private static ConcurrentHashMap<String, Long> lastUpdateMap = new ConcurrentHashMap<>();

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   TEMPERATURE-HUMIDITY BASE STATION");
        System.out.println("   Central Monitoring Hub for Sensor Network");
        System.out.println("============================================================");
        
        // Start monitoring dashboard in separate thread
        startMonitoringDashboard();
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[BASE STATION] Started on port " + PORT);
            System.out.println("[BASE STATION] Waiting for sensor connections...");
            System.out.println("------------------------------------------------------------");
            
            // ========== MAIN SERVER LOOP ==========
            while (true) {
                // Accept sensor branch connection
                Socket sensorSocket = serverSocket.accept();
                
                // Handle each sensor in a new thread
                Thread sensorHandler = new Thread(() -> handleSensorConnection(sensorSocket));
                sensorHandler.start();
            }
            
        } catch (IOException e) {
            System.err.println("[BASE STATION ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================================================
    // SENSOR CONNECTION HANDLER
    // ========================================================
    /**
     * Handles communication with a connected sensor branch.
     * Receives sensor ID and sends back temperature, humidity, and status.
     * 
     * @param sensorSocket Socket connection to the sensor
     */
    private static void handleSensorConnection(Socket sensorSocket) {
        String sensorId = "UNKNOWN";
        
        try (
            ObjectInputStream in = new ObjectInputStream(sensorSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(sensorSocket.getOutputStream())
        ) {
            // ========== RECEIVE SENSOR DATA ==========
            // Sensor branch sends its ID and readings
            SensorData receivedData = (SensorData) in.readObject();
            sensorId = receivedData.getSensorId();
            double temperature = receivedData.getTemperature();
            double humidity = receivedData.getHumidity();
            
            System.out.println("[RECEIVED] Sensor: " + sensorId + 
                             " | Temp: " + temperature + "°C" + 
                             " | Humidity: " + humidity + "%");
            
            // ========== UPDATE DYNAMIC HASHMAPS ==========
            // Store the received readings in HashMaps
            updateSensorData(sensorId, temperature, humidity);
            
            // ========== CALCULATE STATUS ==========
            String status = StatusCalculator.calculateStatus(temperature, humidity);
            statusMap.put(sensorId, status);
            
            // ========== PREPARE RESPONSE ==========
            // Create response with temperature, humidity, and calculated status
            SensorData responseData = new SensorData(sensorId, temperature, humidity, status);
            
            // ========== SEND RESPONSE TO SENSOR ==========
            out.writeObject(responseData);
            out.flush();
            
            System.out.println("[RESPONSE] Sent to " + sensorId + " | Status: " + status);
            
            // Log status message if not normal
            if (!status.equals(StatusCalculator.STATUS_NORMAL)) {
                String message = StatusCalculator.getStatusMessage(status, temperature, humidity);
                System.out.println("[ALERT] " + sensorId + ": " + message);
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SENSOR ERROR] " + sensorId + ": " + e.getMessage());
        } finally {
            try {
                sensorSocket.close();
            } catch (IOException e) {
                // Ignore close errors
            }
        }
    }

    // ========================================================
    // HASHMAP UPDATE METHOD
    // ========================================================
    /**
     * Updates the dynamic HashMaps with new sensor readings.
     * This method is synchronized for thread safety.
     * 
     * @param sensorId Unique sensor identifier
     * @param temperature Temperature reading
     * @param humidity Humidity reading
     */
    private static synchronized void updateSensorData(String sensorId, double temperature, double humidity) {
        // Update temperature HashMap
        temperatureMap.put(sensorId, temperature);
        
        // Update humidity HashMap
        humidityMap.put(sensorId, humidity);
        
        // Update last update timestamp
        lastUpdateMap.put(sensorId, System.currentTimeMillis());
        
        System.out.println("[HASHMAP UPDATE] Total sensors tracked: " + temperatureMap.size());
    }

    // ========================================================
    // MONITORING DASHBOARD
    // ========================================================
    /**
     * Starts a background thread that periodically displays
     * the current state of all sensors.
     */
    private static void startMonitoringDashboard() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                displayDashboard();
            }
        }, 30000, 30000); // Display every 30 seconds
    }

    /**
     * Displays a summary of all sensor readings.
     */
    private static void displayDashboard() {
        if (temperatureMap.isEmpty()) {
            return; // No sensors to display
        }
        
        System.out.println("\n============================================================");
        System.out.println("   📊 SENSOR MONITORING DASHBOARD");
        System.out.println("============================================================");
        System.out.printf("   %-15s %-12s %-12s %-10s%n", "SENSOR ID", "TEMP (°C)", "HUMIDITY (%)", "STATUS");
        System.out.println("------------------------------------------------------------");
        
        for (String sensorId : temperatureMap.keySet()) {
            double temp = temperatureMap.getOrDefault(sensorId, 0.0);
            double humidity = humidityMap.getOrDefault(sensorId, 0.0);
            String status = statusMap.getOrDefault(sensorId, "UNKNOWN");
            
            String statusIcon = status.equals("NORMAL") ? "✅" : 
                               status.equals("WARNING") ? "⚡" : "⚠️";
            
            System.out.printf("   %-15s %-12.1f %-12.1f %s %s%n", 
                            sensorId, temp, humidity, statusIcon, status);
        }
        
        System.out.println("============================================================\n");
    }

    // ========================================================
    // DATA ACCESS METHODS (for potential extensions)
    // ========================================================
    
    /**
     * Get current temperature for a sensor
     */
    public static Double getTemperature(String sensorId) {
        return temperatureMap.get(sensorId);
    }

    /**
     * Get current humidity for a sensor
     */
    public static Double getHumidity(String sensorId) {
        return humidityMap.get(sensorId);
    }

    /**
     * Get current status for a sensor
     */
    public static String getStatus(String sensorId) {
        return statusMap.get(sensorId);
    }

    /**
     * Get all registered sensor IDs
     */
    public static Set<String> getAllSensorIds() {
        return new HashSet<>(temperatureMap.keySet());
    }
}
