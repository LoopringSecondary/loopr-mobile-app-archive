package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.MarketPair;
import leaf.prod.walletsdk.model.response.relay.PageWrapper2;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:25 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class UserFillsParam {

    private String owner;

    private MarketPair marketPair;

    private String sort;

    private PageWrapper2 paging;
}
