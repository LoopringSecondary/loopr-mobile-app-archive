/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:27 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenMetadata {

    private TokenStatus type;

    private TokenType status;

    private String symbol;

    private String name;

    private String address;

    private String unit;

    private Integer decimals;

    private Integer precision;

    private BurnRate burnRate;
}
