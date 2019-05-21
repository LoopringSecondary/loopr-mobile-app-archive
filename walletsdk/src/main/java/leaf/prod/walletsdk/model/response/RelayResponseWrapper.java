package leaf.prod.walletsdk.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelayResponseWrapper<T> {

    private String id;

    private String jsonrpc;

    private T result;

    private RelayError error;
}
