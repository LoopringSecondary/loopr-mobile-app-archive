/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-12 6:09 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitOrderParam {

    private String delegateAddress;

    private String protocol;

    private String sourceId;

    private String owner;

    private String market;

    private String tokenB;

    private String tokenS;

    private String amountB;

    private String amountS;

    private String validSince;

    private String validUntil;

    private String lrcFee;

    private Boolean buyNoMoreThanAmountB;

    private String side;

    private String hash;

    private String walletAddress;

    private String authPrivateKey;

    private String authAddr;

    private Integer marginSplitPercentage;

    private String orderType;

    private String p2pSide;

    private Integer powNonce;

    private Integer v;

    private String r;

    private String s;
}
