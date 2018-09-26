package leaf.prod.app.net;

/**
 * Title:ApiException
 */

public class ApiException extends Exception {

    public String code;

    public String message;

    public ApiException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }
}
