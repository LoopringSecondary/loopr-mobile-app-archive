/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-29 2:23 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity.market;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.adapter.ViewPageAdapter;
import leaf.prod.app.fragment.market.MarketDepthFragment;
import leaf.prod.app.fragment.market.MarketHistoryFragment;
import leaf.prod.app.presenter.market.MarketDetailPresenter;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.TradeType;
import leaf.prod.walletsdk.model.Trend;

public class MarketDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.market_tab)
    TabLayout tradeTab;

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.btn_buy)
    public Button buyButton;

    @BindView(R.id.btn_sell)
    public Button sellButton;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    @BindView(R.id.chart)
    public CandleStickChart chart;

    private List<Fragment> fragments;

    private MarketOrderDataManager orderDataManager;

    private MarketPriceDataManager priceDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        clLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initPresenter() {
        orderDataManager = MarketOrderDataManager.getInstance(this);
        priceDataManager = MarketPriceDataManager.getInstance(this);
        presenter = new MarketDetailPresenter(this, this, orderDataManager.getTradePair());
    }

    @Override
    public void initTitle() {
        title.setBTitle(orderDataManager.getTradePair());
        title.clickLeftGoBack(getWContext());
        title.setDropdownImageButton(R.mipmap.icon_dropdown, button -> {
            finish();
            getOperation().forwardUp(MarketSelectActivity.class);
        });
        title.setRightImageButton(R.mipmap.icon_order_history, button -> getOperation().forward(MarketRecordsActivity.class));
    }

    @Override
    public void initView() {
        String[] titles = new String[2];
        titles[0] = getString(R.string.order_book);
        titles[1] = getString(R.string.dealt_order);
        fragments = new ArrayList<>();
        fragments.add(0, new MarketDepthFragment());
        fragments.add(1, new MarketHistoryFragment());
        setupChart();
        setupViewPager(titles);
        setupButtons();
    }

    private void setupChart() {
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void setupButtons() {
        buyButton.setText(getString(R.string.buy_token, orderDataManager.getTokenA()));
        sellButton.setText(getString(R.string.sell_token, orderDataManager.getTokenA()));
    }

    private void setupViewPager(String[] titles) {
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), fragments, titles));
        tradeTab.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    MarketDepthFragment fragment = (MarketDepthFragment) fragments.get(position);
                    fragment.updateAdapter();
                } else if (position == 1) {
                    MarketHistoryFragment fragment = (MarketHistoryFragment) fragments.get(position);
                    fragment.updateAdapter();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void initData() {
    }

    private void updateKLineCharts() {
        List<Trend> trends = priceDataManager.getTrends();
    }

    public void updateAdapter() {
        updateKLineCharts();
    }

    public void updateAdapter(int index) {
        if (index == 0) {
            MarketDepthFragment depthFragment = (MarketDepthFragment) fragments.get(0);
            if (depthFragment != null) {
                depthFragment.updateAdapter();
            }
        } else if (index == 1) {
            MarketHistoryFragment historyFragment = (MarketHistoryFragment) fragments.get(1);
            if (historyFragment != null) {
                historyFragment.updateAdapter();
            }
        }
    }

    @OnClick({R.id.btn_buy, R.id.btn_sell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_buy:
                orderDataManager.setType(TradeType.buy);
                getOperation().forward(MarketTradeActivity.class);
                break;
            case R.id.btn_sell:
                orderDataManager.setType(TradeType.sell);
                getOperation().forward(MarketTradeActivity.class);
                break;
        }
    }
}
