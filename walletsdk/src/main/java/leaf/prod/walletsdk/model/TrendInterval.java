/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:32
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model;

import com.google.gson.annotations.SerializedName;

public enum TrendInterval {

    @SerializedName("1Hr")
    ONE_HOUR("1Hr"),

    @SerializedName("2Hr")
    TWO_HOURS("2Hr"),

    @SerializedName("4Hr")
    FOUR_HOURS("4Hr"),

    @SerializedName("1Day")
    ONE_DAY("1Day"),

    @SerializedName("1Week")
    ONE_WEEK("1Week");

    private String description;

    TrendInterval(String description) {
        this.description = description;
    }

    public static TrendInterval getInterval(String description) {
        TrendInterval result = TrendInterval.ONE_DAY;
        for (TrendInterval interval : TrendInterval.values()) {
            if (interval.getDescription().equalsIgnoreCase(description)) {
                result = interval;
                break;
            }
        }
        return result;
    }

    public String getDescription() {
        return description;
    }
}
