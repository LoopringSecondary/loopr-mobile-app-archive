/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-21 6:40 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.crawler;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Blog implements Serializable {

    private String title;

    private String url;

    private String imageUrl;
}
