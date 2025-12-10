package RPC.RPCWeatherService;

import java.io.Serializable;

/**
 * ============================================================
 * RPC RESPONSE CLASS
 * ============================================================
 * Represents a Remote Procedure Call response.
 * Contains the result data and success/error status.
 * ============================================================
 */
public class RPCResponse implements Serializable {
    
    // ========== RESPONSE ATTRIBUTES ==========
    private boolean success;        // Whether the RPC call was successful
    private Object result;          // The result data from the method call
    private String errorMessage;    // Error message if call failed

    // ========== CONSTRUCTORS ==========
    
    // Constructor for successful response
    public RPCResponse(Object result) {
        this.success = true;
        this.result = result;
        this.errorMessage = null;
    }

    // Constructor for error response
    public RPCResponse(boolean success, String errorMessage) {
        this.success = success;
        this.result = null;
        this.errorMessage = errorMessage;
    }

    // ========== GETTER METHODS ==========
    public boolean isSuccess() { return success; }
    public Object getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
}
