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
import leaf.prod.app.adapter.NoDataAdapter;
import leaf.prod.app.adapter.market.MarketHistoryAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.common.NoDataType;
import leaf.prod.walletsdk.model.order.Fill;

public class MarketHistoryFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private NoDataAdapter emptyAdapter;

    private MarketHistoryAdapter marketAdapter;

    private MarketPriceDataManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_market_history, container, false);
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
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        marketAdapter = new MarketHistoryAdapter(R.layout.adapter_item_market_history, null);
        recyclerView.setAdapter(marketAdapter);
        emptyAdapter = new NoDataAdapter(R.layout.adapter_item_no_data, null, NoDataType.market_history);
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
        if (marketAdapter != null) {
            List<Fill> orderFills = manager.getFills();
            if (orderFills == null || orderFills.size() == 0) {
                recyclerView.setAdapter(emptyAdapter);
                emptyAdapter.refresh();
            } else {
                recyclerView.setAdapter(marketAdapter);
                marketAdapter.setNewData(orderFills);
                marketAdapter.notifyDataSetChanged();
            }
        }
    }
}
