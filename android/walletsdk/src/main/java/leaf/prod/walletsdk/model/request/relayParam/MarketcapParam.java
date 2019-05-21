package leaf.prod.walletsdk.model.request.relayParam;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class MarketcapParam {

    @NonNull
    private String currency;

    private String token;
}
