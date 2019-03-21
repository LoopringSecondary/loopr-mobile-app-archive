/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:37 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Market {

    private MarketMetadata metadata;

    private MarketTicker ticker;

    public void convert() {
        this.metadata.getMarketPair().convert();
        this.ticker.convert();
    }
}
