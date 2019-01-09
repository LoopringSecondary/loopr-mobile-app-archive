package leaf.prod.app.fragment.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import leaf.prod.app.adapter.market.MarketDepthAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;

public class MarketDepthFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view_buy)
    public RecyclerView recyclerViewBuy;

    @BindView(R.id.recycler_view_sell)
    public RecyclerView recyclerViewSell;

    private MarketPriceDataManager manager;

    private Map<String, RecyclerView> recyclerViews;

    private Map<String, MarketDepthAdapter> adapters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_market_depth, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        manager = MarketPriceDataManager.getInstance(getContext());
    }

    @Override
    protected void initView() {
        adapters = new HashMap<>();
        recyclerViews = new HashMap<>();
        recyclerViews.put("buy", recyclerViewBuy);
        recyclerViews.put("sell", recyclerViewSell);
    }

    @Override
    protected void initData() {
        for (Map.Entry<String, RecyclerView> item : recyclerViews.entrySet()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            MarketDepthAdapter marketAdapter = new MarketDepthAdapter(R.layout.adapter_item_market_depth, null, item.getKey());
            marketAdapter.setOnItemClickListener((adapter, view, position) -> {
                getOperation().addParameter("depth", manager.getDepths(item.getKey()).get(position));
                //            getOperation().forward(P2PRecordDetailActivity.class);
            });
            item.getValue().setAdapter(marketAdapter);
            item.getValue().setLayoutManager(layoutManager);
            adapters.put(item.getKey(), marketAdapter);
        }
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

    public void updateAdapter() {
        for (Map.Entry<String, MarketDepthAdapter> item : adapters.entrySet()) {
            if (item != null && item.getKey() != null && item.getValue() != null) {
                List<String[]> depths = manager.getDepths(item.getKey());
                item.getValue().setNewData(depths);
                item.getValue().notifyDataSetChanged();
            }
        }
    }
}
