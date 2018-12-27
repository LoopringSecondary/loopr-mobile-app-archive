package leaf.prod.app.fragment.market;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import leaf.prod.app.activity.trade.P2PRecordDetailActivity;
import leaf.prod.app.adapter.market.MarketsAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.presenter.market.MarketFragmentPresenter;
import leaf.prod.walletsdk.manager.MarketPriceDataManager;
import leaf.prod.walletsdk.model.MarketsType;
import leaf.prod.walletsdk.model.Ticker;

public class MarketsFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    public RefreshLayout refreshLayout;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    public MarketsAdapter marketAdapter;

    Unbinder unbinder;

    private MarketsType marketsType;

    private MarketFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_markets, container, false);
        unbinder = ButterKnife.bind(this, layout);
        clLoading.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            presenter.refreshTickers();
        });
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        marketAdapter = new MarketsAdapter(R.layout.adapter_item_p2p_record, null, this);
        recyclerView.setAdapter(marketAdapter);
        marketAdapter.setOnItemClickListener((adapter, view, position) -> {
            getOperation().addParameter("ticker", getTickers().get(position));
            getOperation().forward(P2PRecordDetailActivity.class);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.refreshTickers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setMarketsType(MarketsType marketsType) {
        this.marketsType = marketsType;
    }

    public List<Ticker> getTickers() {
        MarketPriceDataManager manager = MarketPriceDataManager.getInstance(getContext());
        return manager.getTickers();
    }
}
