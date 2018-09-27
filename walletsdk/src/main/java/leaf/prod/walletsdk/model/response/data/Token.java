package leaf.prod.walletsdk.model.response.data;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class Token {

    private String protocol;

    private String symbol;

    private String name;

    private String source;

    private int time;

    private boolean deny;

    private BigDecimal decimals;

    private String isMarket;

    private Object icoPrice;

    private int imageResId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Token token = (Token) o;
        return Objects.equals(symbol, token.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
