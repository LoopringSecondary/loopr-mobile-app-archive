package leaf.prod.walletsdk.model.response;

import lombok.Data;

@Data
public class RelayResponseWrapper<T> {

    private String id;

    private String jsonrpc;

    private T result;

    private RelayError error;
}
