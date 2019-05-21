/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-29 4:15 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.market;

import android.content.Context;

import leaf.prod.app.activity.market.MarketDetailActivity;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;

public class MarketDetailPresenter extends BasePresenter<MarketDetailActivity> {

    private String market;

    private MarketPriceDataManager marketManager;

    public MarketDetailPresenter(MarketDetailActivity view, Context context, String market) {
        super(view, context);
        this.market = market;
        this.marketManager = MarketPriceDataManager.getInstance(context);
        this.getTrend();
        this.getDepths();
        this.getFills();
    }

    // socket io
    private void getTrend() {

    }

    // socket io
    private void getDepths() {

    }

    // socket io
    private void getFills() {
    }
}
