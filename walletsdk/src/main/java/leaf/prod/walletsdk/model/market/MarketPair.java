/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 11:44 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import com.google.gson.annotations.Expose;

import leaf.prod.walletsdk.manager.TokenDataManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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

    public void convert() {
        this.baseSymbol = TokenDataManager.getTokenWithProtocol(baseToken).getSymbol();
        this.quoteSymbol = TokenDataManager.getTokenWithProtocol(quoteToken).getSymbol();
    }
}
