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
public class NewsResp {

    private List<News> newsList;

    private Integer pageIndex;

    private Integer pageSize;

    @Data
    @Builder
    private static class News {

        private String currency;

        private String language;

        private String category;

        private String url;

        private String publishTime;

        private String source;

        private String author;

        private String imageUrl;

        private Integer bullIndex;

        private Integer bearIndex;

        private Integer forwardNum;
    }
}
