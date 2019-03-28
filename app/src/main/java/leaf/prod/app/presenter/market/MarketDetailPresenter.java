/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-12-29 4:15 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;
import android.view.View;

import leaf.prod.app.activity.market.MarketDetailActivity;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.Depth;
import leaf.prod.walletsdk.model.OrderFill;
import leaf.prod.walletsdk.model.Trend;
import leaf.prod.walletsdk.model.market.MarketInterval;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketDetailPresenter extends BasePresenter<MarketDetailActivity> {

    private String market;

    private MarketPriceDataManager marketManager;

    public MarketDetailPresenter(MarketDetailActivity view, Context context, String market) {
        super(view, context);
        this.market = market;
        this.marketManager = MarketPriceDataManager.getInstance(context);
        this.getTrend();
        this.getDepths();
        this.getOrderFills();
    }

    private void getTrend() {
        for (MarketInterval interval : MarketInterval.values()) {
            marketManager.getLoopringService().getTrend(market, interval)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Trend>>() {
                        @Override
                        public void onCompleted() {
                            view.clLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.clLoading.setVisibility(View.GONE);
                            unsubscribe();
                        }

                        @Override
                        public void onNext(List<Trend> trends) {
                            marketManager.convertTrend(trends);
                            view.updateAdapter();
                            view.clLoading.setVisibility(View.GONE);
                            unsubscribe();
                        }
                    });
        }
    }

    private void getDepths() {
        marketManager.getLoopringService().getDepths(market, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Depth>() {
                    @Override
                    public void onCompleted() {
                        view.clLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }

                    @Override
                    public void onNext(Depth result) {
                        marketManager.convertDepths(result);
                        view.updateAdapter(0);
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }

    private void getOrderFills() {
        marketManager.getLoopringService().getOrderFills(market, "buy")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OrderFill>>() {
                    @Override
                    public void onCompleted() {
                        view.clLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }

                    @Override
                    public void onNext(List<OrderFill> result) {
                        marketManager.convertOrderFills(result);
                        view.updateAdapter(1);
                        unsubscribe();
                    }
                });
    }
}
