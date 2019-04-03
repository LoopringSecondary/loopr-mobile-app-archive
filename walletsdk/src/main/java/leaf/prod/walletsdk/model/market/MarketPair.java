/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:44 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import java.util.Objects;

import com.google.gson.annotations.Expose;

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MarketPair {

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String baseToken;

    // token name e.g. lrc
    @Expose(serialize = false, deserialize = false)
    private String baseSymbol;

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    private String quoteToken;

    // token name e.g. lrc
    @Expose(serialize = false, deserialize = false)
    private String quoteSymbol;

    public MarketPair convert() {
        this.baseSymbol = TokenDataManager.getTokenWithProtocol(baseToken).getSymbol();
        this.quoteSymbol = TokenDataManager.getTokenWithProtocol(quoteToken).getSymbol();
        return this;
    }

    public MarketPair(String baseSymbol, String quoteSymbol) {
        this.baseSymbol = baseSymbol;
        this.quoteSymbol = quoteSymbol;
        this.baseToken = TokenDataManager.getTokenWithSymbol(baseSymbol).getProtocol();
        this.quoteToken = TokenDataManager.getTokenWithSymbol(quoteSymbol).getProtocol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MarketPair that = (MarketPair) o;
        return Objects.equals(baseToken, that.baseToken);
    }

    @Override
    public String toString() {
        return baseSymbol + " / " + quoteSymbol;
    }
}
