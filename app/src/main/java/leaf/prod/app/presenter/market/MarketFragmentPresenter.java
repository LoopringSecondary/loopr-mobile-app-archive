package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;

import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.TickerSource;
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
        marketManager.getLoopringService()
                .getTickers(TickerSource.coinmarketcap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Ticker>>() {
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
                    public void onNext(List<Ticker> result) {
                        marketManager.convertTickers(result);
                        view.updateAdapter();
                        view.refreshLayout.finishRefresh(true);
                        unsubscribe();
                    }
                });
    }
}
