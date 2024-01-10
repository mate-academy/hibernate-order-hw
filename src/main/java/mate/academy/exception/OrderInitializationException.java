package mate.academy.exception;

public class OrderInitializationException extends RuntimeException {
    public OrderInitializationException(String message) {
        super(message);
    }

    public OrderInitializationException(String message, Throwable e) {
        super(message, e);
    }
}
