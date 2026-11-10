package api;

public class ApiIndisponivelException extends Exception {
    public ApiIndisponivelException(String message) {
        super(message);
    }

    public ApiIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
