package RPC.RPCWeatherService;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

/**
 * ============================================================
 * RPC WEATHER SERVER
 * ============================================================
 * Server that provides weather data through RPC-style calls.
 * Demonstrates Remote Procedure Call pattern using Java sockets.
 * 
 * Supported RPC Methods:
 *   - getWeather(cityName): Returns weather data for a city
 *   - getAllCities(): Returns list of available cities
 * ============================================================
 */
public class WeatherServer {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 5001;
    
    // ========== WEATHER DATA STORAGE ==========
    // HashMap storing base weather data for cities
    private static HashMap<String, double[]> cityWeatherData = new HashMap<>();
    
    // ========== STATIC INITIALIZATION BLOCK ==========
    static {
        // Format: {baseTemperature, baseHumidity}
        cityWeatherData.put("Colombo", new double[]{28.0, 75.0});
        cityWeatherData.put("Kandy", new double[]{24.0, 70.0});
        cityWeatherData.put("Galle", new double[]{27.0, 80.0});
        cityWeatherData.put("Jaffna", new double[]{30.0, 65.0});
        cityWeatherData.put("Nuwara Eliya", new double[]{16.0, 85.0});
    }

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RPC WEATHER SERVER STARTING");
        System.out.println("============================================");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            System.out.println("Available cities: " + cityWeatherData.keySet());
            System.out.println("--------------------------------------------");
            
            // ========== MAIN SERVER LOOP ==========
            while (true) {
                // Accept incoming client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                
                // Handle the RPC request
                handleRPCRequest(clientSocket);
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== RPC REQUEST HANDLER ==========
    private static void handleRPCRequest(Socket clientSocket) {
        try (
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            // Read the RPC request from client
            RPCRequest request = (RPCRequest) in.readObject();
            System.out.println("RPC Request: " + request.getMethodName());
            
            // Process the request and generate response
            RPCResponse response = processRequest(request);
            
            // Send response back to client
            out.writeObject(response);
            out.flush();
            
            System.out.println("Response sent successfully");
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ========== REQUEST PROCESSOR ==========
    private static RPCResponse processRequest(RPCRequest request) {
        String methodName = request.getMethodName();
        Object[] params = request.getParameters();
        
        switch (methodName) {
            case "getWeather":
                // Get weather for specified city
                if (params != null && params.length > 0) {
                    String city = (String) params[0];
                    return getWeatherForCity(city);
                }
                return new RPCResponse(false, "City name required");
                
            case "getAllCities":
                // Return list of all available cities
                return new RPCResponse(cityWeatherData.keySet().toArray(new String[0]));
                
            default:
                return new RPCResponse(false, "Unknown method: " + methodName);
        }
    }

    // ========== WEATHER DATA GENERATOR ==========
    private static RPCResponse getWeatherForCity(String city) {
        if (!cityWeatherData.containsKey(city)) {
            return new RPCResponse(false, "City not found: " + city);
        }
        
        double[] baseData = cityWeatherData.get(city);
        Random random = new Random();
        
        // Generate dynamic weather with slight variations
        double temperature = baseData[0] + (random.nextDouble() * 6 - 3); // ±3°C variation
        double humidity = baseData[1] + (random.nextDouble() * 10 - 5);   // ±5% variation
        
        // Determine weather condition based on temperature and humidity
        String condition = determineCondition(temperature, humidity);
        
        // Get current timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        WeatherData weatherData = new WeatherData(city, temperature, humidity, condition, timestamp);
        return new RPCResponse(weatherData);
    }

    // ========== CONDITION DETERMINER ==========
    private static String determineCondition(double temp, double humidity) {
        if (humidity > 80) return "Rainy";
        if (temp > 32) return "Hot & Sunny";
        if (temp < 18) return "Cool";
        if (humidity < 50) return "Dry";
        return "Pleasant";
    }
}
