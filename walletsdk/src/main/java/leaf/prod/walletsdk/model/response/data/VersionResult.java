package leaf.prod.walletsdk.model.response.data;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-16 2:40 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class VersionResult {

    private Integer id;

    private Long createdAt;

    private Long updatedAt;

    private String version;

    private String description;

    private String baiduUri;

    private String googleUri;
}
