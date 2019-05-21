/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-26 下午6:00
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.token.Token;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTokenParam {

    private boolean requireMetadata;

    private boolean requireInfo;

    private boolean requirePrice;

    private String quoteCurrencyForPrice;

    private Token[] tokens;
}
