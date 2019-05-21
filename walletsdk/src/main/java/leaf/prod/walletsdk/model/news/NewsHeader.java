package leaf.prod.walletsdk.model.news;

import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-24 10:40 AM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class NewsHeader {

    public NewsType newsType;

    public String title;

    public String description;

    public NewsPageWrapper newsList;

    public enum NewsType {
        NEWS_INFO, NEWS_FLASH
    }
}
