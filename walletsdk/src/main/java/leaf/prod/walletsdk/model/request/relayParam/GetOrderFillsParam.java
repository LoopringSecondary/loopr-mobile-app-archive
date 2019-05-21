package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetOrderFillsParam {

    private String delegateAddress;

    private String market;

    private String side;
}
