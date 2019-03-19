/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:32
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.Transaction;

import com.google.gson.annotations.SerializedName;

public enum TxType {
    @SerializedName("TOKEN_AUTH")
    AUTH("Auth"),

    @SerializedName(value = "ETHER_TRANSFER_OUT", alternate = {"TOKEN_TRANSFER_OUT"})
    SEND("Send"),

    @SerializedName(value = "ETHER_TRANSFER_IN", alternate = {"TOKEN_TRANSFER_IN"})
    RECEIVE("Receive"),

    @SerializedName("TRADE_SELL")
    SELL("Sell"),

    @SerializedName("TRADE_BUY")
    BUY("Buy"),

    @SerializedName("ETHER_WRAP")
    WRAP("Wrap"),

    @SerializedName("ETHER_UNWRAP")
    UNWRAP("Unwrap"),

    @SerializedName("ORDER_CANCEL")
    CANCEL("Cancel"),

    @SerializedName("unsupported_contract")
    UNSUPPORTED("Unsupported"),

    @SerializedName("unknown")
    OTHER("Other");

    private String description;

    TxType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
