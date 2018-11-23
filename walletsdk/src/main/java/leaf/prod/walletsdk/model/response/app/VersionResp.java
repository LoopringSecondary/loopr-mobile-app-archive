/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-23 2:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.app;

import lombok.Data;

@Data
public class VersionResp {

    private Integer id;

    private Long createdAt;

    private Long updatedAt;

    private String version;

    private String description;

    private String baiduUri;

    private String googleUri;
}
