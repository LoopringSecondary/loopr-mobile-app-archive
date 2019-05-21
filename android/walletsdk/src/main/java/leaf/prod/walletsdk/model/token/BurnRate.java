/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:28 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurnRate {

    private Double forMarket;

    private Double forP2P;
}
