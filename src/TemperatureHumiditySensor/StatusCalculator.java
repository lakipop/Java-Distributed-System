package TemperatureHumiditySensor;

/**
 * ============================================================
 * STATUS CALCULATOR UTILITY CLASS
 * ============================================================
 * Utility class to determine sensor status based on 
 * temperature and humidity readings.
 * 
 * Status Levels:
 *   - NORMAL: Readings within acceptable range
 *   - WARNING: Readings approaching dangerous levels
 *   - CRITICAL: Readings at dangerous levels requiring action
 * 
 * Thresholds:
 *   Temperature:
 *     - CRITICAL: < 0°C or > 45°C
 *     - WARNING: < 15°C or > 35°C
 *     - NORMAL: 15°C to 35°C
 *   
 *   Humidity:
 *     - CRITICAL: < 10% or > 90%
 *     - WARNING: < 30% or > 70%
 *     - NORMAL: 30% to 70%
 * ============================================================
 */
public class StatusCalculator {

    // ========== TEMPERATURE THRESHOLDS ==========
    private static final double TEMP_CRITICAL_LOW = 0.0;     // Below 0°C is critical
    private static final double TEMP_CRITICAL_HIGH = 45.0;   // Above 45°C is critical
    private static final double TEMP_WARNING_LOW = 15.0;     // Below 15°C is warning
    private static final double TEMP_WARNING_HIGH = 35.0;    // Above 35°C is warning

    // ========== HUMIDITY THRESHOLDS ==========
    private static final double HUMIDITY_CRITICAL_LOW = 10.0;  // Below 10% is critical
    private static final double HUMIDITY_CRITICAL_HIGH = 90.0; // Above 90% is critical
    private static final double HUMIDITY_WARNING_LOW = 30.0;   // Below 30% is warning
    private static final double HUMIDITY_WARNING_HIGH = 70.0;  // Above 70% is warning

    // ========== STATUS CONSTANTS ==========
    public static final String STATUS_NORMAL = "NORMAL";
    public static final String STATUS_WARNING = "WARNING";
    public static final String STATUS_CRITICAL = "CRITICAL";

    // ========== MAIN CALCULATION METHOD ==========
    /**
     * Calculate status based on temperature and humidity readings.
     * Returns the most severe status between temperature and humidity assessments.
     * 
     * @param temperature Temperature reading in Celsius
     * @param humidity Humidity reading in percentage
     * @return Status string (NORMAL, WARNING, or CRITICAL)
     */
    public static String calculateStatus(double temperature, double humidity) {
        // Get individual status for temperature and humidity
        String tempStatus = getTemperatureStatus(temperature);
        String humidityStatus = getHumidityStatus(humidity);
        
        // Return the more severe status
        if (tempStatus.equals(STATUS_CRITICAL) || humidityStatus.equals(STATUS_CRITICAL)) {
            return STATUS_CRITICAL;
        }
        if (tempStatus.equals(STATUS_WARNING) || humidityStatus.equals(STATUS_WARNING)) {
            return STATUS_WARNING;
        }
        return STATUS_NORMAL;
    }

    // ========== TEMPERATURE STATUS HELPER ==========
    /**
     * Determine status based on temperature reading only.
     * 
     * @param temperature Temperature in Celsius
     * @return Temperature-based status
     */
    private static String getTemperatureStatus(double temperature) {
        // Check for critical temperature
        if (temperature < TEMP_CRITICAL_LOW || temperature > TEMP_CRITICAL_HIGH) {
            return STATUS_CRITICAL;
        }
        // Check for warning temperature
        if (temperature < TEMP_WARNING_LOW || temperature > TEMP_WARNING_HIGH) {
            return STATUS_WARNING;
        }
        // Temperature is normal
        return STATUS_NORMAL;
    }

    // ========== HUMIDITY STATUS HELPER ==========
    /**
     * Determine status based on humidity reading only.
     * 
     * @param humidity Humidity percentage (0-100)
     * @return Humidity-based status
     */
    private static String getHumidityStatus(double humidity) {
        // Check for critical humidity
        if (humidity < HUMIDITY_CRITICAL_LOW || humidity > HUMIDITY_CRITICAL_HIGH) {
            return STATUS_CRITICAL;
        }
        // Check for warning humidity
        if (humidity < HUMIDITY_WARNING_LOW || humidity > HUMIDITY_WARNING_HIGH) {
            return STATUS_WARNING;
        }
        // Humidity is normal
        return STATUS_NORMAL;
    }

    // ========== STATUS MESSAGE GENERATOR ==========
    /**
     * Generate a descriptive message based on the status.
     * 
     * @param status Status string
     * @param temperature Temperature reading
     * @param humidity Humidity reading
     * @return Descriptive message about the conditions
     */
    public static String getStatusMessage(String status, double temperature, double humidity) {
        StringBuilder message = new StringBuilder();
        
        switch (status) {
            case STATUS_CRITICAL:
                message.append("⚠️ CRITICAL ALERT! ");
                if (temperature < TEMP_CRITICAL_LOW) {
                    message.append("Extremely cold temperature detected. ");
                } else if (temperature > TEMP_CRITICAL_HIGH) {
                    message.append("Extremely high temperature detected. ");
                }
                if (humidity < HUMIDITY_CRITICAL_LOW) {
                    message.append("Air is extremely dry. ");
                } else if (humidity > HUMIDITY_CRITICAL_HIGH) {
                    message.append("Air is extremely humid. ");
                }
                break;
                
            case STATUS_WARNING:
                message.append("⚡ WARNING: ");
                if (temperature < TEMP_WARNING_LOW || temperature > TEMP_WARNING_HIGH) {
                    message.append("Temperature outside optimal range. ");
                }
                if (humidity < HUMIDITY_WARNING_LOW || humidity > HUMIDITY_WARNING_HIGH) {
                    message.append("Humidity outside optimal range. ");
                }
                break;
                
            default:
                message.append("✅ All readings within normal range.");
        }
        
        return message.toString();
    }
}
