package leaf.prod.app.fragment.market;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.market.MarketDetailActivity;
import leaf.prod.app.adapter.market.MarketSelectAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.walletsdk.manager.MarketOrderDataManager;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.market.Market;
import leaf.prod.walletsdk.model.market.MarketsType;

public class MarketSelectFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private MarketsType marketsType;

    private MarketSelectAdapter marketAdapter;

    private MarketOrderDataManager marketManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_market_select, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        marketManager = MarketOrderDataManager.getInstance(getContext());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        marketAdapter = new MarketSelectAdapter(R.layout.adapter_item_market_select, null);
        recyclerView.setAdapter(marketAdapter);
        marketAdapter.setOnItemClickListener((adapter, view, position) -> {
            Market market = getTickers().get(position);
            marketManager.setTokenBuy(market.getMarketPair().getBaseSymbol());
            marketManager.setTokenSell(market.getMarketPair().getQuoteSymbol());
            getActivity().finish();
            getOperation().forwardDown(MarketDetailActivity.class);
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

    public void updateAdapter(List<Market> markets) {
        if (marketAdapter != null) {
            marketAdapter.setNewData(markets);
            marketAdapter.notifyDataSetChanged();
        }
    }

    public void setMarketsType(MarketsType marketsType) {
        this.marketsType = marketsType;
    }
}
