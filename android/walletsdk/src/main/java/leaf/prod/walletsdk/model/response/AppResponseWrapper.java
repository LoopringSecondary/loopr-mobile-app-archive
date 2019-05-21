package leaf.prod.walletsdk.model.response;

import lombok.Data;

@Data
public class AppResponseWrapper<T> {

    private Boolean success;

    private T message;
}
