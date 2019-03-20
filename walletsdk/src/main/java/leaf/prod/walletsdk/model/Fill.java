package leaf.prod.walletsdk.model;

import org.web3j.utils.Numeric;

import leaf.prod.walletsdk.util.StringUtils;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:07 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class Fill {

    private String owner;

    private String orderHash;

    private String ringHash;

    private Long ringIndex;

    private Long fillIndex;

    private String txHash;

    private String amountS;

    private Double amountSDouble;

    private String amountB;

    private Double amountBDouble;

    private String tokenS;

    private String symbolS;

    private String tokenB;

    private String symbolB;

    private String marketKey;

    private String split;

    private Fee fee;

    private String wallet;

    private String miner;

    private Long blockHeight;

    private Long blockTimestamp;

    public void convert() {
        this.amountBDouble = !StringUtils.isEmpty(this.amountB) ? Numeric.toBigInt(this.amountB).doubleValue() : 0;
        this.amountSDouble = !StringUtils.isEmpty(this.amountS) ? Numeric.toBigInt(this.amountS).doubleValue() : 0;
    }
}
