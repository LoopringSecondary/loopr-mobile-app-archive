package leaf.prod.walletsdk.exception;

public class IllegalCredentialException extends Exception {

    private static final String MESSAGE = "unlock wallet failed!";

    public IllegalCredentialException() {
        super(MESSAGE);
    }

    public IllegalCredentialException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
