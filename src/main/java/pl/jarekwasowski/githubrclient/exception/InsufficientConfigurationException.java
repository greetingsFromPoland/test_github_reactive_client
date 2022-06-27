package pl.jarekwasowski.githubrclient.exception;

public class InsufficientConfigurationException extends RuntimeException {
    public InsufficientConfigurationException(String message) {
        super(message);
    }
}
