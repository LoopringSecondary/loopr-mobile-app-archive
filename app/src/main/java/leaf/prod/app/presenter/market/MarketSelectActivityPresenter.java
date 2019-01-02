package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketSelectActivity;
import leaf.prod.app.fragment.market.MarketSelectFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.MarketsType;
import leaf.prod.walletsdk.model.Ticker;

public class MarketSelectActivityPresenter extends BasePresenter<MarketSelectActivity> {

    private MarketSelectFragment[] fragments;

    private final MarketPriceDataManager marketManager;

    public MarketSelectActivityPresenter(MarketSelectActivity view, Context context) {
        super(view, context);
        setupFragments();
        marketManager = MarketPriceDataManager.getInstance(context);
    }

    private void setupFragments() {
        fragments = new MarketSelectFragment[MarketsType.values().length];
        FragmentManager manager = view.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (MarketsType type : MarketsType.values()) {
            MarketSelectFragment fragment = new MarketSelectFragment();
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
        MarketSelectFragment fragment = fragments[index];
        fragment.updateAdapter();
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideTab(FragmentTransaction transaction) {
        for (MarketSelectFragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
    }

    private void updateAdapters() {
        for (MarketSelectFragment fragment : fragments) {
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
}
