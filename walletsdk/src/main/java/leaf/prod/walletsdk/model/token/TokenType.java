package leaf.prod.walletsdk.model.token;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:37 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public enum TokenType {

    @SerializedName("TOKEN_TYPE_ERC20")
    ERC20("ERC20"),

    @SerializedName("TOKEN_TYPE_ERC1400")
    ERC1400("ERC1400"),

    @SerializedName("TOKEN_TYPE_ETH")
    ETH("ETH"),

    UNKNOWN("UNKNOWN");

    private final String description;

    TokenType(final String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }
}
