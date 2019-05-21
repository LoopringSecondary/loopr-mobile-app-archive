package leaf.prod.walletsdk.model.common;

/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-29 4:53 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public enum TradeType {
    buy, sell;

    public static TradeType getByIndex(int index) {
        TradeType result = buy;
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.ordinal() == index) {
                result = tradeType;
            }
        }
        return result;
    }
}
