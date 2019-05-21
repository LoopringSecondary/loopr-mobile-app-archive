package leaf.prod.app.presenter.market;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;

import leaf.prod.app.activity.market.MarketSelectActivity;
import leaf.prod.app.fragment.market.MarketSelectFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.Market;

public class MarketSelectActivityPresenter extends BasePresenter<MarketSelectActivity> {

    private List<Fragment> fragments;

    private final MarketPriceDataManager marketManager;

    public MarketSelectActivityPresenter(MarketSelectActivity view, Context context) {
        super(view, context);
        marketManager = MarketPriceDataManager.getInstance(context);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public void updateAdapters() {
        for (Fragment item : fragments) {
            MarketSelectFragment fragment = (MarketSelectFragment) item;
            fragment.updateAdapter();
        }
    }

    public void updateAdapter(boolean isFiltering, List<Market> tickers) {
        marketManager.setFiltering(isFiltering);
        if (isFiltering) {
            marketManager.setFilteredMarkets(tickers);
        }
        updateAdapters();
    }
}
