package mate.academy.exception;

public class RegistrationException extends Exception {
    public RegistrationException(String message, Exception cause) {
        super(message, cause);
    }

    public RegistrationException(String message) {
        super(message);
    }
}
