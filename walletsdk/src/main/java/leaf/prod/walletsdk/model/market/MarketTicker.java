/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:38 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarketTicker {

    private String baseToken;

    private String baseSymbol;

    private String quoteToken;

    private String quoteSymbol;

    private Double exchangeRate;

    private Double price;

    private Double volume24H;

    private Double percentChange1H;

    private Double percentChange24H;

    private Double percentChange7D;

    public void convert() {
        this.baseSymbol = TokenDataManager.getTokenWithProtocol(baseToken).getSymbol();
        this.quoteSymbol = TokenDataManager.getTokenWithProtocol(quoteToken).getSymbol();
    }
}
