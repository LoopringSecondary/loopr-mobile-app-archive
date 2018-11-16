/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    private OriginOrder originOrder;

    private OrderStatus orderStatus;

    private Double dealtAmountB;

    private Double dealtAmountS;

    private Double price;

    private String tradingPair;
}
