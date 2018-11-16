/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:18 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OriginOrder {

    private String delegate;

    private String address;

    private String markett;

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

    private Integer v;

    private String r;

    private String s;
}
