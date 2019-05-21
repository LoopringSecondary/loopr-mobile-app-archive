package leaf.prod.walletsdk.exception;

public class UninitializedException extends RuntimeException {

    public UninitializedException() {
        super("SDK not initialized!");
    }
}
