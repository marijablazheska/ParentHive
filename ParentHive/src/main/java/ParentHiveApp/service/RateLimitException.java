package ParentHiveApp.service;

public class RateLimitException extends RuntimeException {
    private final Integer retryAfterSeconds;

    public RateLimitException(String message) {
        super(message);
        this.retryAfterSeconds = null;
    }

    public RateLimitException(String message, Integer retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
