/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-23 2:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.crawler;

import java.util.ArrayList;
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

    public static NewsPageWrapper emptyBean() {
        return NewsPageWrapper.builder().data(new ArrayList<>()).pageIndex(0).pageSize(10).total(0).build();
    }
}
