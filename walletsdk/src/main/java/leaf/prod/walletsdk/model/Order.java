/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.io.Serializable;

import org.web3j.utils.Numeric;
import com.google.gson.annotations.SerializedName;

import leaf.prod.walletsdk.manager.BalanceDataManager;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.util.NumberUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order implements Serializable {

    @SerializedName(value = "originalOrder", alternate = "originOrder")
    private OriginOrder originOrder;

    @SerializedName(value = "status")
    private OrderStatus orderStatus;

    // e.g. "0xd1235"
    private String dealtAmountB;

    // e.g. 0.2345
    private Double dealtAmountBuy;

    // e.g. "0xd1235"
    private String dealtAmountS;

    // e.g. 0.2345
    private Double dealtAmountSell;

    private String price;

    // e.g. 10.50%
    private String filled;

    public Order convert() {
        this.originOrder.convert();
        this.dealtAmountBuy = TokenDataManager.getDouble(originOrder.getTokenB(), Numeric.toBigInt(dealtAmountB)
                .toString());
        this.dealtAmountSell = TokenDataManager.getDouble(originOrder.getTokenS(), Numeric.toBigInt(dealtAmountS)
                .toString());
        this.price = NumberUtils.format1(originOrder.getAmountBuy() / originOrder.getAmountSell(), BalanceDataManager
                .getPrecision(originOrder.getTokenB()));
        this.filled = NumberUtils.format1(dealtAmountSell / originOrder.getAmountSell() * 100, 2) + "%";
        return this;
    }
}
