package leaf.prod.walletsdk.model.response.relay;

import java.util.List;

import leaf.prod.walletsdk.model.order.RawOrder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:01 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class OrdersResult {

    private Long total;

    private List<RawOrder> orders;

    public void convert() {
        if (orders != null) {
            for (RawOrder order : orders) {
                order.convert();
            }
        }
    }
}
