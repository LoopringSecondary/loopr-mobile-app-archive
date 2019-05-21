/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-23 2:38 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.response.app;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class VersionResp {

    private Integer id;

    @SerializedName(value = "created_at")
    private Long createdAt;

    @SerializedName(value = "updated_at")
    private Long updatedAt;

    private String version;

    private String description;

    @SerializedName(value = "baidu_uri")
    private String baiduUri;

    @SerializedName(value = "google_uri")
    private String googleUri;

    @SerializedName(value = "release_note_chs")
    private String releaseNoteChs;

    @SerializedName(value = "release_note_cht")
    private String releaseNoteCht;

    @SerializedName(value = "release_note_en")
    private String releaseNoteEn;
}
