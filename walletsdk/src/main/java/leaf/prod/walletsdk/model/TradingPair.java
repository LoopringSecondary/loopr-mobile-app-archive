package leaf.prod.walletsdk.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-27 3:48 PM
 * Cooperation: loopring.org 路印协议基金会
 */

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TradingPair {

    private String tokenA;

    private String tokenB;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TradingPair))
            return false;
        TradingPair that = (TradingPair) o;
        return Objects.equals(getDescription(), that.getDescription());
    }
}
