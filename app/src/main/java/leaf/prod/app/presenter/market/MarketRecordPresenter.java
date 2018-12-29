/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-29 4:15 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.market;

import android.content.Context;

import leaf.prod.app.activity.market.MarketRecordsActivity;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;

public class MarketRecordPresenter extends BasePresenter<MarketRecordsActivity> {

    private MarketOrderDataManager marketManager;

    public MarketRecordPresenter(MarketRecordsActivity view, Context context) {
        super(view, context);
        marketManager = MarketOrderDataManager.getInstance(context);
    }
}
