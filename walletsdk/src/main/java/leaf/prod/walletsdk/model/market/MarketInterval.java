/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-19 下午2:32
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.market;

import com.google.gson.annotations.SerializedName;

public enum MarketInterval {

    @SerializedName("OHLC_INTERVAL_ONE_MINUTES")
    ONE_MINUTE("1M"),

    @SerializedName("OHLC_INTERVAL_FIVE_MINUTES")
    FIVE_MINUTES("5M"),

    @SerializedName("OHLC_INTERVAL_FIFTEEN_MINUTES")
    FIFTEEN_MINUTES("15M"),

    @SerializedName("OHLC_INTERVAL_THIRTY_MINUTES")
    THIRTY_MINUTES("30M"),

    @SerializedName("OHLC_INTERVAL_ONE_HOUR")
    ONE_HOUR("1H"),

    @SerializedName("OHLC_INTERVAL_TWO_HOURS")
    TWO_HOURS("2H"),

    @SerializedName("OHLC_INTERVAL_FOUR_HOURS")
    FOUR_HOURS("4H"),

    @SerializedName("OHLC_INTERVAL_TWELVE_HOURS")
    TWELVE_HOURS("12H"),

    @SerializedName("OHLC_INTERVAL_ONE_DAY")
    ONE_DAY("1D"),

    @SerializedName("OHLC_INTERVAL_THREE_DAYS")
    THREE_DAYS("3D"),

    @SerializedName("OHLC_INTERVAL_FIVE_DAYS")
    FIVE_DAYS("5D"),

    @SerializedName("OHLC_INTERVAL_ONE_WEEK")
    ONE_WEEK("1W");

    private String description;

    MarketInterval(String description) {
        this.description = description;
    }

    public static MarketInterval getInterval(String description) {
        MarketInterval result = MarketInterval.ONE_DAY;
        for (MarketInterval interval : MarketInterval.values()) {
            if (interval.getDescription().equalsIgnoreCase(description)) {
                result = interval;
                break;
            }
        }
        return result;
    }

    public static MarketInterval getByName(String name) {
        MarketInterval result = MarketInterval.ONE_DAY;
        for (MarketInterval interval : MarketInterval.values()) {
            if (interval.toString().equalsIgnoreCase(name)) {
                result = interval;
                break;
            }
        }
        return result;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        String result = "1H";
        switch (this) {
            case ONE_HOUR:
                result = "1H";
                break;
            case TWO_HOURS:
                result = "2H";
                break;
            case FOUR_HOURS:
                result = "4H";
                break;
            case ONE_DAY:
                result = "1D";
                break;
            case ONE_WEEK:
                result = "1W";
                break;
        }
        return result;
    }
}
