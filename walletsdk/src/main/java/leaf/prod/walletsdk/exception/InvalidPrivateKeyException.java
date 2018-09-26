package leaf.prod.walletsdk.exception;

public class InvalidPrivateKeyException extends Exception {

    public InvalidPrivateKeyException() {
        super("invalid private key, check the input!");
    }
}
