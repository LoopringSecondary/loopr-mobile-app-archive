/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import java.io.Serializable;

import org.web3j.utils.Numeric;
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Params implements Serializable {

    // 随机地址
    @SerializedName("dualAuthAddr")
    private String dualAuthAddr;

    // 随机私钥
    @SerializedName("dualAuthPrivateKey")
    private String dualAuthPrivateKey;

    // wallet address
    @SerializedName("wallet")
    private String wallet;

    @SerializedName(value = "status")
    private OrderStatus status;

    // hex string e.g. 0x5be8e179
    @SerializedName(value = "validUntil")
    private String validUntil;

    // int value e.g. 3562653865313739
    private Integer validU;

    public void convert() {
        this.validU = Numeric.toBigInt(validUntil).intValue();
    }
}
