package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-27 3:48 PM
 * Cooperation: loopring.org 路印协议基金会
 */

@Data
@Builder
class TradingPair {

    private String tokenA;

    private String tokenB;

    private String description;
}
