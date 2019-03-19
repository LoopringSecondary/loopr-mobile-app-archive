/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:33
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.Transaction;

import com.google.gson.annotations.SerializedName;

public enum TxStatus {
    @SerializedName("pending")
    PENDING("Pending"),

    @SerializedName("success")
    SUCCESS("Success"),

    @SerializedName("failed")
    FAILED("Failed"),

    @SerializedName("unknown")
    OTHER("Other");

    private String description;

    TxStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
