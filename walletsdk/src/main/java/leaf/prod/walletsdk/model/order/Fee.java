package leaf.prod.walletsdk.model.order;

import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.util.StringUtils;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:04 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class Fee {

    private String tokenFee;

    private String amountFee;

    private String feeAmountS;

    private String feeAmountB;

    private Double feeAmountSDouble;

    private Double feeAmountBDouble;

    private String feeRecipient;

    private String waiveFeePercentage;

    private String walletSplitPercentage;

    public void convert() {
        this.feeAmountSDouble = !StringUtils.isEmpty(feeAmountS) ? Numeric.toBigInt(feeAmountS).doubleValue() : 0;
        this.feeAmountBDouble = !StringUtils.isEmpty(feeAmountB) ? Numeric.toBigInt(feeAmountB).doubleValue() : 0;
    }
}
