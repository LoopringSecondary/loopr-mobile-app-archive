/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:29 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.token;

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenTicker {

    // "0xef68e7c694f40c8202821edf525de3782458639f"
    private String token;

    // "lrc"
    private String symbol;

    private Double price;

    private Long volume24H;

    private Double percentChange1H;

    private Double percentChange24H;

    private Double percentChange7D;

    public TokenTicker convert() {
        this.symbol = TokenDataManager.getTokenWithProtocol(token).getSymbol();
        return this;
    }
}
