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
import leaf.prod.walletsdk.util.NumberUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawOrder implements Serializable {

    @SerializedName(value = "hash")
    private String hash;

    @SerializedName(value = "version")
    private Integer version;

    @SerializedName(value = "owner")
    private String owner;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    @SerializedName(value = "tokenB")
    private String tokenB;

    // token name e.g. lrc
    private String tokenBuy;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    @SerializedName(value = "tokenS")
    private String tokenS;

    // token name e.g. lrc
    private String tokenSell;

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    @SerializedName(value = "amountB")
    private Amount amountB;

    // double value e.g. 0.02
    private Double amountBuy;

    // big integer hex string e.g. 0x34f07768a92a83d00000
    @SerializedName(value = "amountS")
    private Amount amountS;

    // double value e.g. 0.02
    private Double amountSell;

    // hex string e.g. 0x5be8e179
    @SerializedName(value = "validSince")
    private String validSince;

    // int value e.g. 3562653865313739
    private Integer validS;

    @SerializedName(value = "params")
    private Params params;

    @SerializedName(value = "feeParams")
    private FeeParams feeParams;

    @SerializedName(value = "state")
    private State state;

    private String buyPrice;

    private String sellPrice;

    // e.g. 10.50%
    private String filled;

    public RawOrder convert() {
        this.params.convert();
        this.feeParams.convert();

        this.tokenBuy = TokenDataManager.getTokenWithProtocol(tokenB).getName();
        this.tokenSell = TokenDataManager.getTokenWithProtocol(tokenS).getName();
        this.amountBuy = TokenDataManager.getDouble(tokenBuy, Numeric.toBigInt(amountB.getValue()).toString());
        this.amountSell = TokenDataManager.getDouble(tokenSell, Numeric.toBigInt(amountS.getValue()).toString());
        this.validS = Numeric.toBigInt(validSince).intValue();

        String stringValue = Numeric.toBigInt(this.state.getOutstandingAmountB().getValue()).toString();
        this.state.setOutstandingAmountBuy(TokenDataManager.getDouble(tokenBuy, stringValue));
        stringValue = Numeric.toBigInt(this.state.getOutstandingAmountS().getValue()).toString();
        this.state.setOutstandingAmountSell(TokenDataManager.getDouble(tokenSell, stringValue));
        stringValue = Numeric.toBigInt(this.state.getOutstandingAmountFee().getValue()).toString();
        this.state.setOutstandingAmountF(TokenDataManager.getDouble(this.feeParams.getTokenF(), stringValue));

        Double rate = amountBuy - this.state.getOutstandingAmountBuy() / amountBuy;
        this.filled = NumberUtils.format1(rate * 100, 2) + "%";

        return this;
    }
}
