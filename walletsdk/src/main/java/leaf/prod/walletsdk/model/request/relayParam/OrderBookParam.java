package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.MarketPair;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:46 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class OrderBookParam {

    private int level;

    private int size;

    private MarketPair marketPair;
}
