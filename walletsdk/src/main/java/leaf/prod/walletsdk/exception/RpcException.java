package leaf.prod.walletsdk.exception;

public class RpcException extends Exception {

    public RpcException(String message) {
        super("Rpc response has error! error:\n" + message);
    }

    public RpcException(Throwable cause) {
        super("Rpc response has error! error:\n", cause);
    }
}
