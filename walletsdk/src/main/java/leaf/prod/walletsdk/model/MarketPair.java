package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Builder
@Data
public class MarketPair {

    private String baseToken;

    private String quoteToken;
}
