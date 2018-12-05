/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:29 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.annotations.SerializedName;

public enum P2PSide {
    @SerializedName("maker")
    MAKER("maker"),

    @SerializedName("taker")
    TAKER("taker"),

    @SerializedName("unknown")
    UNKNOWN("unknown");

    private String description;

    P2PSide(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
