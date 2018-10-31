package leaf.prod.app.model;

import java.io.Serializable;

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

    private ThirdLoginUser language; // todo test

    private String currency;

    private Integer id;

    private ThirdLoginUser config;

    public ThirdLogin(String accountToken, String language, String currency, ThirdLoginUser config) {
        this.accountToken = accountToken;
        this.language = config;
        this.currency = currency;
//        this.config = config;
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

    public ThirdLoginUser getLanguage() {
        return language;
    }

    public void setLanguage(ThirdLoginUser language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ThirdLoginUser getConfig() {
        return config;
    }

    public void setConfig(ThirdLoginUser config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "ThirdLogin{" +
                "updatedAt='" + updatedAt + '\'' +
                ", accountToken='" + accountToken + '\'' +
                ", createAt='" + createAt + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                ", id=" + id +
                ", config='" + config + '\'' +
                '}';
    }
}
