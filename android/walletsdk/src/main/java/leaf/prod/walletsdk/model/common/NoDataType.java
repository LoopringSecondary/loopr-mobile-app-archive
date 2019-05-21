/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-11 下午6:35
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.model.common;

public enum NoDataType {
    asset("asset"),
    transation("transation"),
    p2p_order("p2p_order"),
    market_order("market_order"),
    market_depth_buy("buy"),
    market_depth_sell("sell"),
    market_history("market_history"),
    contact("contact"),
    news("news");

    private final String description;

    NoDataType(final String text) {
        this.description = text;
    }

    public String getDescription() {
        return description;
    }

    public static NoDataType getNoDataType(String description) {
        NoDataType result = NoDataType.asset;
        for (NoDataType noDataType : NoDataType.values()) {
            if (noDataType.description.equalsIgnoreCase(description)) {
                result = noDataType;
                break;
            }
        }
        return result;
    }
}
