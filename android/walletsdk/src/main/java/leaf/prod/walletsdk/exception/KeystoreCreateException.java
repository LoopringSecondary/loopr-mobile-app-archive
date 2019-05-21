package leaf.prod.walletsdk.exception;

public class KeystoreCreateException extends Exception {

    private static String MESSAGE = "unable to create keystore file!";

    public KeystoreCreateException() {
        super(MESSAGE);
    }

    public KeystoreCreateException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
