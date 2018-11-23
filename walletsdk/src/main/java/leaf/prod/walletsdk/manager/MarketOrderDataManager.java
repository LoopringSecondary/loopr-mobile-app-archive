/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-11-16 3:47 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.walletsdk.manager;

import java.util.Map;

import android.content.Context;

import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.service.LoopringService;

public class MarketOrderDataManager extends OrderDataManager {

    private Context context;

    private Map balanceInfo;

    private LoopringService loopringService;

    private static MarketOrderDataManager marketOrderManager = null;

    private MarketOrderDataManager(Context context) {
        this.context = context;
        loopringService = new LoopringService();
    }

    public static MarketOrderDataManager getInstance(Context context) {
        if (marketOrderManager == null) {
            marketOrderManager = new MarketOrderDataManager(context);
        }
        return marketOrderManager;
    }

    public Map verify(OriginOrder order) {
        return null;
    }
}
