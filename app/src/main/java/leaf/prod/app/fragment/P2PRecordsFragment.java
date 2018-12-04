package leaf.prod.app.fragment;

import java.util.ArrayList;
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
import leaf.prod.app.activity.P2PRecordDetailActivity;
import leaf.prod.app.adapter.P2PRecordAdapter;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderStatus;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PType;

public class P2PRecordsFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private P2PRecordAdapter recordAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_p2p_records, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder()
                .tradingPair("LRC-WETH")
                .dealtAmountS(100d)
                .originOrder(OriginOrder.builder()
                        .validS((int) System.currentTimeMillis() / 1000)
                        .amountSell(10000d)
                        .p2pType(P2PType.MAKER)
                        .build())
                .orderStatus(OrderStatus.OPENED)
                .price(2400d)
                .build());
        orders.add(Order.builder()
                .tradingPair("LRC-WETH")
                .dealtAmountS(100d)
                .originOrder(OriginOrder.builder()
                        .validS((int) (System.currentTimeMillis() / 1000))
                        .p2pType(P2PType.TAKER)
                        .amountSell(10000d)
                        .build())
                .orderStatus(OrderStatus.OPENED)
                .price(2400d)
                .build());
        recordAdapter = new P2PRecordAdapter(R.layout.adapter_item_p2p_record, orders);
        recordAdapter.setOnItemClickListener((adapter, view, position) -> {
            view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
                // TODO: 2018/12/4 取消订单
            });
            getOperation().forward(P2PRecordDetailActivity.class);
        });
        recyclerView.setAdapter(recordAdapter);
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
}
