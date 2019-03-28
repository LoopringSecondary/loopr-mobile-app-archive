/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:37 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import leaf.prod.walletsdk.util.NumberUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Market {

    private MarketMetadata metadata;

    private MarketTicker ticker;

    public Market convert() {
        this.metadata.getMarketPair().convert();
        this.ticker.convert();
        return this;
    }

    public String getBaseSymbol() {
        return metadata.getMarketPair().getBaseSymbol();
    }

    public String getQuoteSymbol() {
        return metadata.getMarketPair().getQuoteSymbol();
    }

    public String getVolume() {
        return "Vol " + NumberUtils.numberformat2(ticker.getVolume24H());
    }

    public String getExchangeRate() {
        return NumberUtils.format1(ticker.getExchangeRate(), metadata.getPriceDecimals());
    }

    public String getChange() {
        Double change = ticker.getPercentChange24H();
        String changeStr = NumberUtils.format1(change, 2);
        if (change < 0) {
            changeStr = "↓ " + changeStr.replace("-", "");
        } else {
            changeStr = "↑ " + changeStr;
        }
        return changeStr + "%";
    }

    public MarketPair getMarketPair() {
        return metadata.getMarketPair();
    }
}
