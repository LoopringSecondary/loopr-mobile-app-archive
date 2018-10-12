package leaf.prod.walletsdk.model.response;

import lombok.Data;

@Data
public class ResponseWrapper<T> {

    private String id;

    private String jsonrpc;

    private T result;

    private Error error;

    @Data
    class Error {

        private Long code;

        private String message;
    }

    public String getError() {
        return error.toString();
    }
}
