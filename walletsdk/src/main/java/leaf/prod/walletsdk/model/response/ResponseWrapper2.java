package leaf.prod.walletsdk.model.response;

import lombok.Data;

@Data
public class ResponseWrapper2<T> {

    private Boolean success;

    private T message;
}
