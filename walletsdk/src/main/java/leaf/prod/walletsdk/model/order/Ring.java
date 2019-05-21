package leaf.prod.walletsdk.model.order;

import java.util.List;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:08 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class Ring {

    private String ringHash;

    private Long ringIndex;

    private Long fillsAmount;

    private String miner;

    private String txHash;

    private List<Fee> fees;

    private Long blockHeight;

    private Long blockTimestamp;

    public Ring convert() {
        if(fees != null) {
            for(Fee fee : fees) {
                fee.convert();
            }
        }
        return this;
    }
}
