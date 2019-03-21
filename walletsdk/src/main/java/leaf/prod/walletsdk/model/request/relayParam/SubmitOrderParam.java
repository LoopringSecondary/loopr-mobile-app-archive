/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-12 6:09 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.order.FeeParams;
import leaf.prod.walletsdk.model.order.OrderParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitOrderParam {

    private String sourceId;

    private String owner;

    private Integer version;

    private String tokenB;

    private String tokenS;

    private String amountB;

    private String amountS;

    private String validSince;

    private OrderParams params;

    private FeeParams feeParams;
}
