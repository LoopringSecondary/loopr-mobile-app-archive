/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.JsonObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OriginOrder {

    private String delegate;

    private String owner;

    private String market;

    private String tokenBuy;

    private String tokenSell;

    private Double amountBuy;

    private Double amountSell;

    private Integer validSince;

    private Integer validUntil;

    private Double lrcFee;

    private Boolean buyNoMoreThanAmountB;

    private String side;

    private String hash;

    private String walletAddress;

    private String authPrivateKey;

    private String authAddr;

    private Integer marginSplitPercentage;

    private OrderType orderType;

    private P2PType p2pType;

    private Integer powNonce;

    private Integer v;

    private String r;

    private String s;

    public OriginOrder createOriginOrder(JsonObject json) {
        this.delegate = json.get("delegateAddress").getAsString();
        this.owner = json.get("owner").getAsString();
        this.owner = json.get("owner").getAsString();


//        TokenDataManager.getToken()

//        this.tokenBuy = json.get("owner").getAsString();
//        this.tokenSell = json.get("owner").getAsString();
//        this.amountBuy = json.get("owner").getAsString();
//        this.amountSell = json.get("owner").getAsString();
//        this.validSince = json.get("owner").getAsString();
//        this.validUntil = json.get("owner").getAsString();
//        this.lrcFee = json.get("owner").getAsString();
//        this.buyNoMoreThanAmountB = json.get("owner").getAsString();
//        this.walletAddress = json.get("owner").getAsString();
//        this.authPrivateKey = json.get("owner").getAsString();
//        this.authAddr = json.get("owner").getAsString();
//        this.marginSplitPercentage = json.get("owner").getAsString();
//        this.orderType = json.get("owner").getAsString();
//        this.p2pType = json.get("owner").getAsString();
//        this.powNonce = json.get("owner").getAsString();
        return null;
    }
}
// {"owner":"0x59845c6007df15a5ffd5fee0111d219d764f8536","delegateAddress":"0x17233e07c67d086464fD408148c3ABB56245FA64","protocol":"0x8d8812b72d1e4ffCeC158D25f56748b7d67c1e78","tokenB":"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2","tokenS":"0xef68e7c694f40c8202821edf525de3782458639f","amountB":"0xda475abf00000","amountS":"0x8ac7230489e80000","lrcFee":"0x103caccd13350000","validSince":"0x5bf22c90","validUntil":"0x5bf37e10","marginSplitPercentage":50,"buyNoMoreThanAmountB":false,"walletAddress":"0x56447c02767ba621f103c0f3dbf564dbcacf284b","orderType":"market_order","authAddr":"0x18e3cba14d305acaae3138bf16f3a324c730b532","authPrivateKey":"400e0a078b1d63875586c9f2f2c1949e674f64ac0c553af661e9ce4ca9ff33ab"}
