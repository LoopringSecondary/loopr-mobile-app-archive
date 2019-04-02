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
public class OrderParams implements Serializable {

    // 随机地址
    @SerializedName("dualAuthAddr")
    private String dualAuthAddr;

    // 随机私钥
    @SerializedName("dualAuthPrivateKey")
    private String dualAuthPrivateKey;

    // wallet address
    @SerializedName("wallet")
    private String wallet;

    @SerializedName("status")
    private OrderStatus status;

    // int value e.g. 1548422323
    @SerializedName("validUntil")
    private Integer validUntil;

    // true -- 一次被吃完  false -- 不必
    @Expose(deserialize = false)
    @SerializedName("allOrNone")
    private Boolean allOrNone;

    @Expose(deserialize = false)
    private String sig;
}
