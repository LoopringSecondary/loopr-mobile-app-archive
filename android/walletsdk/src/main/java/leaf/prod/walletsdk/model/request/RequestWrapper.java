package leaf.prod.walletsdk.model.request;

import lombok.Data;

@Data
public class RequestWrapper {

    private String jsonrpc;

    private String method;

    private Object[] params;

    private long id;

    public RequestWrapper(String method, Object... params) {
        this.jsonrpc = "2.0";
        this.method = method;
        this.params = params;
        this.id = System.currentTimeMillis();
    }
}
