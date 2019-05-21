package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.market.MarketPair;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetMarketsParam {

    private boolean requireMetadata;

    private boolean requireTicker;

    private String quoteCurrencyForTicker;

    private MarketPair[] marketPairs;
}
