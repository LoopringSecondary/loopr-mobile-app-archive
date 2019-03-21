package leaf.prod.walletsdk.model.request.relayParam;

import leaf.prod.walletsdk.model.common.Paging;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-03-18 4:58 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class RingParam {

    private String sort;

    private Paging paging;

    private Filter filter;

    @Data
    @Builder
    public static class Filter {

        private int ringIndex;
    }
}
