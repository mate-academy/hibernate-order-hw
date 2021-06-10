package mate.academy.exception;

public class RegisterException extends Exception {
    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterException(String message) {
        super(message);
    }
}
