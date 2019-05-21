package leaf.prod.walletsdk.model.market;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2019-03-21 2:12 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public enum MarketStatus {

    @SerializedName(value = "ACTIVE")
    ACTIVE,

    @SerializedName(value = "READONLY")
    READONLY,

    @SerializedName(value = "TERMINATED")
    TERMINATED,

    UNKNOWN
}
