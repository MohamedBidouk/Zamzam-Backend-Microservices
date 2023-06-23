package org.zamzam.refrigeratorservice.exception;

public class RefrigeratorNotFoundException extends RuntimeException{
    public RefrigeratorNotFoundException(){super();}
    public RefrigeratorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefrigeratorNotFoundException(String message) {
        super(message);
    }

    public RefrigeratorNotFoundException(Throwable cause) {
        super(cause);
    }
}
