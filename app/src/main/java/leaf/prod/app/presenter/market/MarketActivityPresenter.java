package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import leaf.prod.app.activity.market.MarketsActivity;
import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.response.relay.MarketsResult;
import leaf.prod.walletsdk.util.CurrencyUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketActivityPresenter extends BasePresenter<MarketsActivity> {

    private List<Fragment> fragments;

    private final MarketPriceDataManager marketManager;

    public MarketActivityPresenter(MarketsActivity view, Context context) {
        super(view, context);
        marketManager = MarketPriceDataManager.getInstance(context);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    private void updateAdapters() {
        for (Fragment item : fragments) {
            MarketsFragment fragment = (MarketsFragment) item;
            fragment.updateAdapter();
        }
    }

    public void updateAdapter(boolean isFiltering, List<Market> markets) {
        marketManager.setFiltering(isFiltering);
        if (isFiltering) {
            marketManager.setFilteredMarkets(markets);
        }
        updateAdapters();
    }

    public void refreshTickers() {
        view.clLoading.setVisibility(View.VISIBLE);
        marketManager.getRelayService()
                .getMarkets(true, true, CurrencyUtil.getCurrency(context), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MarketsResult>() {
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
                    public void onNext(MarketsResult result) {
                        marketManager.convertMarkets(result.getMarkets());
                        updateAdapters();
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }
}
