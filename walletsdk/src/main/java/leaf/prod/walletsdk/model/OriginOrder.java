/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.JsonObject;

import leaf.prod.walletsdk.manager.TokenDataManager;
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

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String tokenB;

    // token protocol e.g. lrc
    private String tokenBuy;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String tokenS;

    // token protocol e.g. lrc
    private String tokenSell;

    // big integer hex string e.g. 0x34f07768a92a83d00000
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

    private Integer marginSplitPercentage;

    private OrderType orderType;

    private P2PType p2pType;

    private Integer powNonce;

    private Integer v;

    private String r;

    private String s;

    private OriginOrder() {
    }

    public static OriginOrder createOriginOrder(JsonObject json) {
        OriginOrder order = new OriginOrder();
        order.delegate = json.get("delegateAddress").getAsString();
        order.owner = json.get("owner").getAsString();
        order.tokenB = json.get("tokenB").getAsString();
        order.tokenBuy = TokenDataManager.getToken(order.tokenB).getSymbol();
        order.tokenS = json.get("tokenS").getAsString();
        order.tokenSell = TokenDataManager.getToken(order.tokenS).getSymbol();
        order.amountB = json.get("amountB").getAsString();
        order.amountBuy = TokenDataManager.getDouble(order.tokenBuy, order.amountB);
        order.amountS = json.get("amountS").getAsString();
        order.amountSell = TokenDataManager.getDouble(order.tokenSell, order.amountS);
        order.validSince = json.get("validSince").getAsString();
        order.validS = Integer.parseInt(order.validSince, 16);
        order.validUntil = json.get("validUntil").getAsString();
        order.validU = Integer.parseInt(order.validUntil, 16);
        order.lrcFee = json.get("lrcFee").getAsString();
        order.lrc = TokenDataManager.getDouble("LRC", order.lrcFee);
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
