/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-21 6:40 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.crawler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class News {

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
