package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketsActivity;
import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.MarketsType;
import leaf.prod.walletsdk.model.Ticker;
import leaf.prod.walletsdk.model.TickerSource;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MarketActivityPresenter extends BasePresenter<MarketsActivity> {

    private MarketsFragment[] fragments;

    private final MarketPriceDataManager marketManager;

    public MarketActivityPresenter(MarketsActivity view, Context context) {
        super(view, context);
        setupFragments();
        marketManager = MarketPriceDataManager.getInstance(context);
    }

    private void setupFragments() {
        fragments = new MarketsFragment[MarketsType.values().length];
        FragmentManager manager = view.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (MarketsType type : MarketsType.values()) {
            MarketsFragment fragment = new MarketsFragment();
            fragment.setMarketsType(type);
            fragments[type.ordinal()] = fragment;
            transaction.add(R.id.main_frame, fragment, type.name());
        }
        transaction.commitAllowingStateLoss();
    }

    public void setTabSelect(int index) {
        FragmentManager manager = view.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideTab(transaction);
        MarketsFragment fragment = fragments[index];
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideTab(FragmentTransaction transaction) {
        for (MarketsFragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
    }

    private void updateAdapters() {
        for (MarketsFragment fragment : fragments) {
            fragment.updateAdapter();
        }
    }

    public void updateAdapter(boolean isFiltering, List<Ticker> tickers) {
        marketManager.setFiltering(isFiltering);
        if (isFiltering) {
            marketManager.setFilteredTickers(tickers);
        }
        updateAdapters();
    }

    public void refreshTickers() {
        marketManager.getLoopringService()
                .getTickers(TickerSource.coinmarketcap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Ticker>>() {
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
                    public void onNext(List<Ticker> result) {
                        marketManager.convertTickers(result);
                        updateAdapters();
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }
}
