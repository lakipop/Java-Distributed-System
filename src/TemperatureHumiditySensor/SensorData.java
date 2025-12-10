package TemperatureHumiditySensor;

import java.io.Serializable;

/**
 * ============================================================
 * SENSOR DATA CLASS
 * ============================================================
 * Serializable data class representing sensor readings.
 * Contains temperature, humidity, and calculated status.
 * 
 * Used for:
 *   - Transferring sensor readings between branch and base
 *   - Storing sensor data in base station HashMaps
 * ============================================================
 */
public class SensorData implements Serializable {
    
    // ========== SENSOR ATTRIBUTES ==========
    private String sensorId;        // Unique sensor identifier
    private double temperature;     // Temperature in Celsius
    private double humidity;        // Humidity percentage (0-100)
    private String status;          // Calculated status (NORMAL/WARNING/CRITICAL)
    private long timestamp;         // Reading timestamp

    // ========== CONSTRUCTORS ==========
    
    /**
     * Full constructor with all fields
     */
    public SensorData(String sensorId, double temperature, double humidity, String status) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Constructor without status (status calculated later by base station)
     */
    public SensorData(String sensorId, double temperature, double humidity) {
        this(sensorId, temperature, humidity, "PENDING");
    }

    // ========== GETTER METHODS ==========
    public String getSensorId() { return sensorId; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public String getStatus() { return status; }
    public long getTimestamp() { return timestamp; }

    // ========== SETTER FOR STATUS ==========
    public void setStatus(String status) { this.status = status; }

    // ========== STRING REPRESENTATION ==========
    @Override
    public String toString() {
        return String.format(
            "Sensor[%s] Temp: %.1f°C | Humidity: %.1f%% | Status: %s",
            sensorId, temperature, humidity, status
        );
    }
}
