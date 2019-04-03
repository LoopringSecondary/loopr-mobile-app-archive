/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:34 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import java.io.Serializable;

import org.web3j.utils.Numeric;
import com.google.gson.annotations.Expose;
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
    @Expose(serialize = false, deserialize = false)
    private String tokenBuy;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    @SerializedName(value = "tokenS")
    private String tokenS;

    // token name e.g. lrc
    @Expose(serialize = false, deserialize = false)
    private String tokenSell;

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    @SerializedName(value = "amountB")
    private String amountB;

    // double value e.g. 0.02
    @Expose(serialize = false, deserialize = false)
    private Double amountBuy;

    // big integer hex string e.g. 0x34f07768a92a83d00000
    @SerializedName(value = "amountS")
    private String amountS;

    // double value e.g. 0.02
    @Expose(serialize = false, deserialize = false)
    private Double amountSell;

    // int value e.g. 3562653865313739
    @SerializedName(value = "validSince")
    private Integer validSince;

    @SerializedName(value = "params")
    private OrderParams params;

    @SerializedName(value = "feeParams")
    private FeeParams feeParams;

    private Erc1400Params erc1400Params;

    @SerializedName(value = "state")
    private OrderState state;

    @Expose(serialize = false, deserialize = false)
    private String buyPrice;

    @Expose(serialize = false, deserialize = false)
    private String sellPrice;

    // e.g. 10.50%
    private String filled;

    public OrderStatus getStatus() {
        return state.getStatus();
    }

    public RawOrder convert() {
        this.feeParams.convert();

        this.tokenBuy = TokenDataManager.getTokenWithProtocol(tokenB).getSymbol();
        this.tokenSell = TokenDataManager.getTokenWithProtocol(tokenS).getSymbol();
        this.amountBuy = TokenDataManager.getDouble(tokenBuy, Numeric.toBigInt(amountB).toString());
        this.amountSell = TokenDataManager.getDouble(tokenSell, Numeric.toBigInt(amountS).toString());

        String stringValue = Numeric.toBigInt(this.state.getOutstandingAmountB()).toString();
        this.state.setOutstandingAmountBuy(TokenDataManager.getDouble(tokenBuy, stringValue));
        stringValue = Numeric.toBigInt(this.state.getOutstandingAmountS()).toString();
        this.state.setOutstandingAmountSell(TokenDataManager.getDouble(tokenSell, stringValue));
        stringValue = Numeric.toBigInt(this.state.getOutstandingAmountFee()).toString();
        this.state.setOutstandingAmountF(TokenDataManager.getDouble(this.feeParams.getTokenF(), stringValue));

        Double rate = (amountBuy - this.state.getOutstandingAmountBuy()) / amountBuy;
        this.filled = NumberUtils.format1(rate * 100, 2) + "%";

        return this;
    }
}
