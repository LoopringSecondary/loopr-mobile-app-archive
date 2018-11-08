package leaf.prod.app.model;

import java.io.Serializable;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:28
 * Cooperation: Loopring
 */
public class ThirdLogin implements Serializable {

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("account_token")
    private String accountToken;

    @SerializedName("created_at")
    private String createAt;

    private Integer id;

    private String config;

    private Boolean success;

    public ThirdLogin(String accountToken, String config) {
        this.accountToken = accountToken;
        this.config = config;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfig() {
        return config;
    }

    public ThirdLoginUser getThirdLoginUser() {
        try {
            return new Gson().fromJson(this.config, ThirdLoginUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ThirdLogin that = (ThirdLogin) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, config);
    }

    @Override
    public String toString() {
        return "ThirdLogin{" +
                "updatedAt='" + updatedAt + '\'' +
                ", accountToken='" + accountToken + '\'' +
                ", createAt='" + createAt + '\'' +
                ", id=" + id +
                ", config='" + config + '\'' +
                ", success=" + success +
                '}';
    }
}
