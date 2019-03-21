package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NonceParam {

    private String owner;
}
