/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-23 2:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.crawler;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsPageWrapper {

    private List<News> data;

    private Integer pageIndex;

    private Integer pageSize;

    private Integer total;
}
