/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-12 6:09 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.market.MarketPair;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelOrdersParam {

    private String id;

    private MarketPair marketPair;

    private Integer time;

    private String owner;

    private String sig;
}
