/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:29 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.order;

import com.google.gson.annotations.SerializedName;

public enum OrderType {

    @SerializedName("market_order")
    MARKET("market_order"),

    @SerializedName("p2p_order")
    P2P("p2p_order"),

    UNKONWN("unknown");

    private String description;

    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
