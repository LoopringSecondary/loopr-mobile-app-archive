package leaf.prod.walletsdk.pojo.loopring.request.param;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class MarketcapParam {

    @NonNull
    private Currency currency;

    public enum Currency {
        CNY,
        USD,
        BTC
    }
}
