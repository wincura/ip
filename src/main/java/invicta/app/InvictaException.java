package invicta.app;

/**
 * Represents exceptions related to invalid user input and other chatbot-specific errors.
 */
public class InvictaException extends Exception {
    // Declaring a basic constructor that takes in error message to be displayed
    public InvictaException(String message) {
        super(message);
    }

    public InvictaException(String message, Throwable cause) {
        super(message, cause);
    }
}
