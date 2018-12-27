package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDepthsParam {

    private String delegateAddress;

    private String market;

    private Integer length;
}
