package RPC.RPCWeatherService;

import java.io.Serializable;

/**
 * ============================================================
 * RPC REQUEST CLASS
 * ============================================================
 * Represents a Remote Procedure Call request.
 * Contains the method name and parameters to be executed on server.
 * ============================================================
 */
public class RPCRequest implements Serializable {
    
    // ========== REQUEST ATTRIBUTES ==========
    private String methodName;      // Name of the remote method to call
    private Object[] parameters;    // Parameters for the method

    // ========== CONSTRUCTOR ==========
    public RPCRequest(String methodName, Object[] parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    // ========== GETTER METHODS ==========
    public String getMethodName() { return methodName; }
    public Object[] getParameters() { return parameters; }
}
