package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.response.relay.PageWrapper2;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 5:13 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class ActivityParam {

    private String owner;

    private String token;

    private PageWrapper2 paging;
}
