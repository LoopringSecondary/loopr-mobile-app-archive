package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.market.MarketInterval;
import leaf.prod.walletsdk.model.market.MarketPair;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:27 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class MarketHistoryParam {

    private MarketPair marketPair;

    private MarketInterval interval;

    private long beginTime;

    private long endTime;
}
