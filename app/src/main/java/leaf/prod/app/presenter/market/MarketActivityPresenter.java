package leaf.prod.app.presenter.market;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketsActivity;
import leaf.prod.app.fragment.market.MarketsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.model.MarketsType;

public class MarketActivityPresenter extends BasePresenter<MarketsActivity> {

    private MarketsFragment[] fragments;

    public MarketActivityPresenter(MarketsActivity view, Context context) {
        super(view, context);
        ButterKnife.bind(this, view);
        setupFragments();
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
}
