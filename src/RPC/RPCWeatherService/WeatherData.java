package RPC.RPCWeatherService;

import java.io.Serializable;

/**
 * ============================================================
 * WEATHER DATA CLASS
 * ============================================================
 * Serializable data class representing weather information.
 * Used for transferring weather data between client and server.
 * ============================================================
 */
public class WeatherData implements Serializable {
    
    // ========== WEATHER ATTRIBUTES ==========
    private String city;
    private double temperature;
    private double humidity;
    private String condition;
    private String timestamp;

    // ========== CONSTRUCTOR ==========
    public WeatherData(String city, double temperature, double humidity, String condition, String timestamp) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    // ========== GETTER METHODS ==========
    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public String getCondition() { return condition; }
    public String getTimestamp() { return timestamp; }

    // ========== STRING REPRESENTATION ==========
    @Override
    public String toString() {
        return String.format("Weather for %s: %.1f°C, Humidity: %.1f%%, Condition: %s, Time: %s",
                city, temperature, humidity, condition, timestamp);
    }
}
