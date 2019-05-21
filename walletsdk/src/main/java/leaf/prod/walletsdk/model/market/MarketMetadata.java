/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:36 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarketMetadata {

    private MarketStatus marketStatus;

    private Integer priceDecimals;

    private Integer orderbookAggLevels;

    private Integer precisionForAmount;

    private Integer precisionForTotal;

    private Boolean browsableInWallet;

    private MarketPair marketPair;

    private String marketHash;
}
