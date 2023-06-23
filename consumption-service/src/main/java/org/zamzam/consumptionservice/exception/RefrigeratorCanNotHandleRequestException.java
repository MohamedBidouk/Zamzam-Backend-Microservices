package org.zamzam.consumptionservice.exception;

public class RefrigeratorCanNotHandleRequestException extends RuntimeException{
    public RefrigeratorCanNotHandleRequestException() {
        super();
    }

    public RefrigeratorCanNotHandleRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefrigeratorCanNotHandleRequestException(String message) {
        super(message);
    }

    public RefrigeratorCanNotHandleRequestException(Throwable cause) {
        super(cause);
    }
}
