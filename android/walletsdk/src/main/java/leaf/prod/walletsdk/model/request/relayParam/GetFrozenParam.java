package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetFrozenParam {

    private String delegateAddress;

    private String owner;
}
