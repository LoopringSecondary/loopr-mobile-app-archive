/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.presenter.market.MarketTradeActivityPresenter;
import leaf.prod.app.views.TitleView;

public class MarketTradeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.trade_tab)
    TabLayout tradeTab;

    private MarketTradeActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_market_trade);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        presenter = new MarketTradeActivityPresenter(this, this);
        presenter.setTabSelect(0);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.p2p_title));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        tradeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.setTabSelect(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tradeTab.getTabAt(getIntent().getIntExtra("tag", 0)).select();
    }

    @Override
    public void initData() {
    }
}
