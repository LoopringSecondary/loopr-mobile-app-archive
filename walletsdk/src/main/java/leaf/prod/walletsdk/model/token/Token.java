/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:34 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.token;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    private TokenMetadata metadata;

    private TokenInfo info;

    private TokenTicker ticker;

    private int imageResId;

    public String getSymbol() {
        return this.metadata.getSymbol();
    }

    public String getName() {
        return this.metadata.getName();
    }

    public String getProtocol() {
        return this.metadata.getAddress();
    }

    public BigDecimal getDecimals() {
        return BigDecimal.valueOf(Math.pow(10, metadata.getDecimals()));
    }

    public Token convert() {
        this.ticker.convert();
        return this;
    }
}
