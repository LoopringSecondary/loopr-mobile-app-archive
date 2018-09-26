/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:32
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.annotations.SerializedName;

public enum TxType {
    @SerializedName("approve")
    APPROVE("Approve"),

    @SerializedName("send")
    SEND("Send"),

    @SerializedName("receive")
    RECEIVE("Receive"),

    @SerializedName("sell")
    SELL("Sell"),

    @SerializedName("buy")
    BUY("Buy"),

    @SerializedName("convert_income")
    CONVERT_INCOME("Convert"),

    @SerializedName("convert_outcome")
    CONVERT_OUTCOME("Convert"),

    @SerializedName("cancel_order")
    CANCEL("Cancel"),

    @SerializedName("cutoff")
    CUTOFF("Cancel"),

    @SerializedName("unsupported_contract")
    UNSUPPORTED("Other"),

    @SerializedName("unknown")
    OTHER("Unknown");

    private String description;

    TxType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
