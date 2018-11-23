/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OriginOrder {

    private String delegate;

    private String owner;

    private String market;

    private String tokenBuy; // token protocol

    private String tokenSell; // token protocol

    private String amountBuy; // big integer hex string

    private String amountSell; // big integer hex string

    private String validSince; // hex string

    private String validUntil; // hex string

    private String lrcFee; // big integer hex string

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

    private OriginOrder() { }

    public static OriginOrder createOriginOrder(JsonObject json) {
        OriginOrder order = new OriginOrder();
        order.delegate = json.get("delegateAddress").getAsString();
        order.owner = json.get("owner").getAsString();
        order.tokenBuy = json.get("tokenB").getAsString();
        order.tokenSell = json.get("tokenS").getAsString();
        order.amountBuy = json.get("amountB").getAsString();
        order.amountSell = json.get("amountS").getAsString();
        order.validSince = json.get("validSince").getAsString();
        order.validUntil = json.get("validUntil").getAsString();
        order.lrcFee = json.get("lrcFee").getAsString();
        order.buyNoMoreThanAmountB = json.get("buyNoMoreThanAmountB").getAsBoolean();
        order.walletAddress = json.get("walletAddress").getAsString();
        order.authPrivateKey = json.get("authPrivateKey").getAsString();
        order.authAddr = json.get("authAddr").getAsString();
        order.marginSplitPercentage = json.get("marginSplitPercentage").getAsInt();
        order.getType(json);
        order.powNonce = 1;
        return order;
    }

    private void getType(JsonObject json) {
        String p2pType = json.get("p2pSide").getAsString();
        String orderType = json.get("orderType").getAsString();
        this.p2pType = p2pType == null ? P2PType.UNKNOWN : P2PType.valueOf(p2pType);
        this.orderType = orderType == null ? OrderType.UNKONWN : OrderType.valueOf(orderType);
        if (this.orderType == OrderType.P2P) {
            this.side = this.p2pType == P2PType.MAKER ? "sell" : "buy";
        } else {
            this.side = json.get("side").getAsString();
        }
    }
}
