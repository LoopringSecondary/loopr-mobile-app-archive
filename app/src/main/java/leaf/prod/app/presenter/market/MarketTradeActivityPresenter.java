package leaf.prod.app.presenter.market;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketTradeActivity;
import leaf.prod.app.fragment.market.MarketTradeFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.model.TradeType;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:42 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class MarketTradeActivityPresenter extends BasePresenter<MarketTradeActivity> {

    private MarketOrderDataManager orderDataManager;
    private MarketTradeFragment[] fragments;

    public MarketTradeActivityPresenter(MarketTradeActivity view, Context context) {
        super(view, context);
        orderDataManager = MarketOrderDataManager.getInstance(context);
        setupFragments();
    }

    private void setupFragments() {
        fragments = new MarketTradeFragment[TradeType.values().length];
        FragmentManager manager = view.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (TradeType type : TradeType.values()) {
            MarketTradeFragment fragment = new MarketTradeFragment();
            fragment.setTradeType(type);
            fragments[type.ordinal()] = fragment;
            transaction.add(R.id.main_frame, fragment, type.name());
        }
        transaction.commitAllowingStateLoss();
    }

    public void setTabSelect(int index) {
        FragmentManager manager = view.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideTab(transaction);
        MarketTradeFragment fragment = fragments[index];
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
        orderDataManager.setType(TradeType.getByIndex(index));
    }

    private void hideTab(FragmentTransaction transaction) {
        for (MarketTradeFragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
    }
}
