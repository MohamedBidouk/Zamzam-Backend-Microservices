package org.zamzam.consumptionservice.exception;

public class ReachedDailyLimitException extends RuntimeException{
    public ReachedDailyLimitException() {
        super();
    }

    public ReachedDailyLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReachedDailyLimitException(String message) {
        super(message);
    }

    public ReachedDailyLimitException(Throwable cause) {
        super(cause);
    }
}
