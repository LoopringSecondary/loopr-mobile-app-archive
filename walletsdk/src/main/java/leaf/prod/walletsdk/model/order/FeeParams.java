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

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeeParams implements Serializable {

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    @SerializedName(value = "tokenFee")
    private String tokenFee;

    // token name e.g. lrc
    private String tokenF;

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    @SerializedName(value = "amountFee")
    private Amount amountFee;

    // double value e.g. 0.02
    private Double amountF;

    @SerializedName(value = "tokenBFeePercentage")
    private Integer tokenBFeePercentage;

    @SerializedName(value = "tokenSFeePercentage")
    private Integer tokenSFeePercentage;

    @SerializedName(value = "walletSplitPercentage")
    private Integer walletSplitPercentage;

    public void convert() {
        this.tokenF = TokenDataManager.getTokenWithProtocol(tokenFee).getName();
        this.amountF = TokenDataManager.getDouble(tokenF, Numeric.toBigInt(amountFee.getValue()).toString());
    }
}
