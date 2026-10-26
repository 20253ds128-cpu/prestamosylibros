package mx.edu.utez.prestamosylibros.exception;

public class GlobalExceptionHandler extends RuntimeException {
    public GlobalExceptionHandler(String message) {
        super(message);
    }
}
