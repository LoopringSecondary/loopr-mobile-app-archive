package leaf.prod.walletsdk.exception;

public class InvalidKeystoreException extends Exception {

    private static String MESSAGE = "invalid keystore!";

    public InvalidKeystoreException() {
        super(MESSAGE);
    }

    public InvalidKeystoreException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
