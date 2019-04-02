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

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderState implements Serializable {

    @SerializedName(value = "status")
    private OrderStatus status;

    // tokens可用于订单数量，暂时无用
    @SerializedName(value = "actualAmountS")
    private String actualAmountS;

    @SerializedName(value = "actualAmountB")
    private String actualAmountB;

    @SerializedName(value = "actualAmountFee")
    private String actualAmountFee;

    // 订单未成交数量，计算fill
    @SerializedName(value = "outstandingAmountS")
    private String outstandingAmountS;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountSell;

    @SerializedName(value = "outstandingAmountB")
    private String outstandingAmountB;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountBuy;

    @SerializedName(value = "outstandingAmountFee")
    private String outstandingAmountFee;

    @Expose(serialize = false, deserialize = false)
    private Double outstandingAmountF;
}
