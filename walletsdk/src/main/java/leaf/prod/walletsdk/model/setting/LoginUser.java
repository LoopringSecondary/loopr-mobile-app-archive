package leaf.prod.walletsdk.model.setting;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:28
 * Cooperation: Loopring
 */
@Data
@Builder
public class LoginUser implements Serializable {

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("account_token")
    private String accountToken;

    @SerializedName("created_at")
    private String createAt;

    private Integer id;

    private String config;

    public UserConfig getUserConfig() {
        try {
            return new Gson().fromJson(this.config, UserConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginUser that = (LoginUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, config);
    }
}
