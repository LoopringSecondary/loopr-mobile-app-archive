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
import leaf.prod.walletsdk.manager.P2POrderDataManager;
import leaf.prod.walletsdk.model.CancelOrder;
import leaf.prod.walletsdk.model.Order;
import leaf.prod.walletsdk.model.OrderStatus;
import leaf.prod.walletsdk.model.OriginOrder;
import leaf.prod.walletsdk.model.P2PSide;
import leaf.prod.walletsdk.model.request.param.NotifyScanParam;
import leaf.prod.walletsdk.service.LoopringService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class P2PRecordsFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private P2PRecordAdapter recordAdapter;

    private LoopringService service;

    private P2POrderDataManager p2pManager;

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
        service = new LoopringService();
        p2pManager = P2POrderDataManager.getInstance(getContext());
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
                        .p2pSide(P2PSide.MAKER)
                        .build())
                .orderStatus(OrderStatus.OPENED)
                .price(2400d)
                .build());
        orders.add(Order.builder()
                .tradingPair("LRC-WETH")
                .dealtAmountS(100d)
                .originOrder(OriginOrder.builder()
                        .validS((int) (System.currentTimeMillis() / 1000))
                        .p2pSide(P2PSide.TAKER)
                        .amountSell(10000d)
                        .build())
                .orderStatus(OrderStatus.OPENED)
                .price(2400d)
                .build());
        recordAdapter = new P2PRecordAdapter(R.layout.adapter_item_p2p_record, orders);
        recordAdapter.setOnItemClickListener((adapter, view, position) -> {
            view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
                String hash = orders.get(position).getOriginOrder().getHash();
                NotifyScanParam.SignParam signParam = p2pManager.genCancelParam();
                CancelOrder order = CancelOrder.builder().orderHash(hash).build();
                service.cancelOrderFlex(order, signParam)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO: yanyan cancel order failed
                            }

                            @Override
                            public void onNext(String s) {
                                // TODO: yanyan cancel order success
                            }
                        });
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
