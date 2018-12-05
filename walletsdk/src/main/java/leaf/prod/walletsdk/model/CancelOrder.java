/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-15 5:58 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelOrder {

    private String timestamp;

    private CancelType type;

    private String orderHash;

    private String tokenS;

    private String tokenB;

    private String owner;

    private Integer cutoff;

    private String hash;

    public boolean isValid() {
        boolean result = timestamp != null;
        switch (this.type) {
            case hash:
                result = orderHash != null;
                break;
            case time:
                result = cutoff != null;
                break;
            case market:
                result = (tokenB != null && tokenS != null);
                break;
        }
        return result;
    }
}
