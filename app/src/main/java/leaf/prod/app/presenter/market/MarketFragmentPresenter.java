package leaf.prod.app.presenter.market;

import android.content.Context;

import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.MarketPair;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.util.CurrencyUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketFragmentPresenter extends BasePresenter<MarketsFragment> {

    private final MarketPriceDataManager marketManager;

    public MarketFragmentPresenter(MarketsFragment view, Context context) {
        super(view, context);
        marketManager = MarketPriceDataManager.getInstance(context);
    }

    public void refreshTickers() {
        marketManager.getRelayService()
                .getMarkets(true, true, CurrencyUtil.getCurrency(context), new MarketPair[0])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MarketsResult>() {
                    @Override
                    public void onCompleted() {
                        view.refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.refreshLayout.finishRefresh(true);
                        unsubscribe();
                    }

                    @Override
                    public void onNext(MarketsResult result) {
                        marketManager.convertMarkets(result.getMarkets());
                        view.updateAdapter();
                        view.refreshLayout.finishRefresh(true);
                        unsubscribe();
                    }
                });
    }
}
