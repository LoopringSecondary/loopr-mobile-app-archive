/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-23 2:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.crawler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexResult {

    private String uuid;

    private Integer bullIndex;

    private Integer bearIndex;

    private Integer forwardNum;
}
