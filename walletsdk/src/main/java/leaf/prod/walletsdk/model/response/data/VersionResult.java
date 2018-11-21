package leaf.prod.walletsdk.model.response.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-16 2:40 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
public class VersionResult {

    private Integer count;

    @SerializedName("app_versions")
    private List<AppVersion> appVersions;

    @Data
    public static class AppVersion {

        @SerializedName("must_update")
        private Boolean mustUpdate;

        private String version;
    }
}
