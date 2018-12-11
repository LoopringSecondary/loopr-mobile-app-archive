package leaf.prod.walletsdk.model;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:28
 * Cooperation: Loopring
 */
@Data
public class LoginUser implements Serializable {

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("account_token")
    private String accountToken;

    @SerializedName("created_at")
    private String createAt;

    private Integer id;

    private String config;

    public LoginUser(String accountToken, String config) {
        this.accountToken = accountToken;
        this.config = config;
    }

    public LoginUserConfig getThirdLoginUser() {
        try {
            return new Gson().fromJson(this.config, LoginUserConfig.class);
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
