package invicta.app;

public class InvictaException extends Exception {
    // Declaring a basic constructor that takes in error message to be displayed
    public InvictaException(String message) {
        super(message);
    }

    public InvictaException(String message, Throwable cause) {
        super(message, cause);
    }
}
