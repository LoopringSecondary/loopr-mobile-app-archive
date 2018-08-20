package com.tomcat360.lyqb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.ReceiveActivity;
import com.tomcat360.lyqb.activity.TokenListActivity;
import com.tomcat360.lyqb.activity.WalletDetailActivity;
import com.tomcat360.lyqb.adapter.MainWalletAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class MainFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    ImageView rightBtn;
    @BindView(R.id.wallet_count)
    TextView walletCount;
    @BindView(R.id.wallet_address)
    TextView walletAddress;
    @BindView(R.id.wallet_qrcode)
    ImageView walletQrcode;
    @BindView(R.id.wallet_scan_tv)
    TextView walletScanTv;
    @BindView(R.id.wallet_receive_tv)
    TextView walletReceiveTv;
    @BindView(R.id.wallet_send_tv)
    TextView walletSendTv;
    @BindView(R.id.wallet_trades_tv)
    TextView walletTradesTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_scan)
    LinearLayout llScan;
    @BindView(R.id.ll_receive)
    LinearLayout llReceive;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.ll_trade)
    LinearLayout llTrade;
    @BindView(R.id.menu_scan)
    LinearLayout menuScan;
    @BindView(R.id.menu_add_assets)
    LinearLayout menuAddAssets;
    @BindView(R.id.menu_wallet)
    LinearLayout menuWallet;
    @BindView(R.id.menu_transaction)
    LinearLayout menuTransaction;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    private MainWalletAdapter mAdapter;

    private boolean showMenu = false;  //判断menu是否显示

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_main, container, false);
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
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        mAdapter = new MainWalletAdapter(R.layout.adapter_item_wallet, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showMenu){
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                }else {
                    getOperation().forward(WalletDetailActivity.class);
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.ll_scan, R.id.ll_receive, R.id.ll_send, R.id.ll_trade,R.id.menu_scan, R.id.menu_add_assets, R.id.menu_wallet, R.id.menu_transaction, R.id.right_btn, R.id.ll_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_scan:  //scan 按钮
                if (showMenu){
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                }else {

                }
                break;
            case R.id.ll_receive://receive 按钮
                if (showMenu){
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                }else {
                    getOperation().forward(ReceiveActivity.class);

                }
                break;
            case R.id.ll_send://send 按钮
                break;
            case R.id.ll_trade://trade 按钮
                break;
            case R.id.right_btn:  //右上角添加按钮
                llMenu.setVisibility(View.VISIBLE);
                showMenu = true;
                break;
            case R.id.ll_main://主布局页面，主要实现点击关闭menu页面
                llMenu.setVisibility(View.GONE);
                showMenu = false;
                break;
            case R.id.menu_scan:
                break;
            case R.id.menu_add_assets:
                getOperation().forward(TokenListActivity.class);
                break;
            case R.id.menu_wallet:
                break;
            case R.id.menu_transaction:
                break;

        }
    }

}
