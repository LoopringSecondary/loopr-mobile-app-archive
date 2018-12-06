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
public class CancelOrderParam {

    private String orderHash;

    private Integer type;

    private Integer cutoff;

    private String tokenS;

    private String tokenB;

    private NotifyScanParam.SignParam sign;
}
