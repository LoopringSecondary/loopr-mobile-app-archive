package leaf.prod.app.fragment.market;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketDetailActivity;
import leaf.prod.app.adapter.market.MarketsAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.presenter.market.MarketFragmentPresenter;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.market.MarketsType;

public class MarketsFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    public RefreshLayout refreshLayout;

    private MarketsAdapter marketAdapter;

    private MarketsType marketsType;

    private MarketFragmentPresenter presenter;

    private MarketOrderDataManager orderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_markets, container, false);
        unbinder = ButterKnife.bind(this, layout);
        refreshLayout.setOnRefreshListener(refreshLayout -> presenter.refreshTickers());
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        this.presenter = new MarketFragmentPresenter(this, getContext());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        orderManager = MarketOrderDataManager.getInstance(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        marketAdapter = new MarketsAdapter(R.layout.adapter_item_markets, null, this);
        recyclerView.setAdapter(marketAdapter);
        marketAdapter.setOnItemClickListener((adapter, view, position) -> {
            Market market = getTickers().get(position);
            orderManager.setTokenSell(market.getBaseSymbol());
            orderManager.setTokenBuy(market.getQuoteSymbol());
            getOperation().forward(MarketDetailActivity.class);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public List<Market> getTickers() {
        List<Market> result = null;
        MarketPriceDataManager manager = MarketPriceDataManager.getInstance(getContext());
        switch (this.marketsType) {
            case Favorite:
                result = manager.getFavTickers();
                break;
            case WETH:
            case LRC:
            case USDT:
            case TUSD:
                result = manager.getMarketsBy(this.marketsType.name());
                break;
        }
        return result;
    }

    public void updateAdapter() {
        updateAdapter(getTickers());
    }

    public void updateAdapter(List<Market> tickers) {
        if (marketAdapter != null) {
            marketAdapter.setNewData(tickers);
            marketAdapter.notifyDataSetChanged();
        }
    }

    public void setMarketsType(MarketsType marketsType) {
        this.marketsType = marketsType;
    }

    public MarketsType getMarketsType() {
        return marketsType;
    }
}
