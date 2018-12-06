/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.io.Serializable;

import org.web3j.utils.Numeric;
import com.google.gson.annotations.SerializedName;

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OriginOrder implements Serializable {

    @SerializedName("delegateAddress")
    private String delegate;

    @SerializedName("address")
    private String owner;

    private String market;

    // token protocol e.g. lrc
    private String tokenB;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String tokenBuy;

    // token protocol e.g. lrc
    private String tokenS;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String tokenSell;

    // integer hex string e.g. "0x34f07768a92a83d00000"
    private String amountB;

    // double value e.g. 0.02
    private Double amountBuy;

    // big integer hex string e.g. 0x34f07768a92a83d00000
    private String amountS;

    // double value e.g. 0.02
    private Double amountSell;

    // int value e.g. 3562653865313739
    private Integer validS;

    // hex string e.g. 0x5be8e179
    private String validSince;

    // int value e.g. 3562653865313739
    private Integer validU;

    // hex string e.g. 0x5be8e179
    private String validUntil;

    // double value e.g. 0.02
    private Double lrc;

    // big integer hex string e.g. 0x34f07768a92a83d00000
    private String lrcFee;

    private Boolean buyNoMoreThanAmountB;

    private String side;

    private String hash;

    private String walletAddress;

    private String authPrivateKey;

    private String authAddr;

    // hex string e.g. 0x32
    private String marginSplitPercentage;

    // integer e.g. 50
    private Integer margin;

    private OrderType orderType;

    private P2PSide p2pSide;

    private Integer powNonce;

    private String v;

    private String r;

    private String s;

    public void convert() {
        this.tokenBuy = TokenDataManager.getToken(tokenB).getProtocol();
        this.tokenSell = TokenDataManager.getToken(tokenS).getProtocol();
        this.amountBuy = TokenDataManager.getDouble(tokenB, amountB);
        this.amountSell = TokenDataManager.getDouble(tokenS, amountS);
        this.validS = Numeric.toBigInt(validSince).intValue();
        this.validS = Numeric.toBigInt(validUntil).intValue();
        this.lrc = TokenDataManager.getDouble("LRC", lrcFee);
        this.margin = Numeric.toBigInt(marginSplitPercentage).intValue();
    }
}
