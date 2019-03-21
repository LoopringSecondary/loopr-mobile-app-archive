/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import leaf.prod.walletsdk.model.common.Amount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderState implements Serializable {

    @SerializedName(value = "status")
    private OrderStatus status;

    // tokens可用于订单数量，暂时无用
    @SerializedName(value = "actualAmountS")
    private Amount actualAmountS;

    @SerializedName(value = "actualAmountB")
    private Amount actualAmountB;

    @SerializedName(value = "actualAmountFee")
    private Amount actualAmountFee;

    // 订单未成交数量，计算fill
    @SerializedName(value = "outstandingAmountS")
    private Amount outstandingAmountS;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountSell;

    @SerializedName(value = "outstandingAmountB")
    private Amount outstandingAmountB;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountBuy;

    @SerializedName(value = "outstandingAmountFee")
    private Amount outstandingAmountFee;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountF;
}
