/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 11:35 AM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus implements Serializable {

    @SerializedName("ORDER_OPENED")
    OPENED("opened"),

    @SerializedName("ORDER_WAIT_SUBMIT_RING")
    WAITED("waited"),

    @SerializedName("ORDER_FINISHED")
    FINISHED("finished"),

    @SerializedName("ORDER_CUTOFF")
    CUTOFF("cutoff"),

    @SerializedName("ORDER_CANCELLED")
    CANCELLED("cancelled"),

    @SerializedName("ORDER_EXPIRE")
    EXPIRED("expired"),

    @SerializedName("ORDER_P2P_LOCKED")
    LOCKED("locked"),

    @SerializedName("ORDER_UNKNOWN")
    UNKNOWN("unknown");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
