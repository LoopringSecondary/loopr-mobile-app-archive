package com.tomcat360.lyqb.fragment;

import java.math.BigDecimal;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyqb.walletsdk.listener.BalanceListener;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.ActivityScanerCode;
import com.tomcat360.lyqb.activity.ReceiveActivity;
import com.tomcat360.lyqb.activity.SendActivity;
import com.tomcat360.lyqb.activity.TokenListActivity;
import com.tomcat360.lyqb.activity.WalletDetailActivity;
import com.tomcat360.lyqb.adapter.MainWalletAdapter;
import com.tomcat360.lyqb.presenter.MainFragmentPresenter;
import com.tomcat360.lyqb.utils.ButtonClickUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 *
 */
public class MainFragment extends BaseFragment {

    public final static int BALANCE_SUCCESS = 1;

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

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

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;

    @SuppressLint("HandlerLeak")
    Handler handlerBalance = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BALANCE_SUCCESS:
                default:
                    break;
            }
        }
    };

    private MainWalletAdapter mAdapter;

    private boolean showMenu = false;  //判断menu是否显示

    private BigDecimal moneyValue;  //钱包总金额

    private BalanceListener balanceListener = new BalanceListener();

    private MainFragmentPresenter presenter;

    private boolean flag = true; //第一次进入

    private String address;

    private List<BalanceResult.Asset> listAsset; //  返回的token列表

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
        });
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        this.presenter = new MainFragmentPresenter(this, this.getContext());
    }

    @Override
    protected void initView() {
        address = (String) SPUtils.get(getContext(), "address", "");
        walletAddress.setText(address);
    }

    @Override
    protected void initData() {
        showProgress("loading");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainWalletAdapter(R.layout.adapter_item_wallet, null, presenter);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (showMenu) {
                llMenu.setVisibility(View.GONE);
                showMenu = false;
            } else {
                getOperation().addParameter("moneyValue", moneyValue.toPlainString());
                getOperation().addParameter("symbol", listAsset.get(position).getSymbol());
                getOperation().forward(WalletDetailActivity.class);
            }
        });
        initToken();
    }

    private void initToken() {
        LyqbLogger.log(address);
        Observable<BalanceResult> observable = balanceListener.start();
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BalanceResult>() {
            @Override
            public void onCompleted() {
                hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                hideProgress();
            }

            @Override
            public void onNext(BalanceResult balanceResult) {
                hideProgress();
                if (balanceResult.getTokens() != null)
                    presenter.setTokenLegalPrice(balanceResult.getTokens());
            }
        });
        balanceListener.queryByOwner(address);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag) {
            flag = false;
        } else {
            presenter.updateListToken(listAsset);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_scan, R.id.ll_receive, R.id.ll_send, R.id.ll_trade, R.id.menu_scan, R.id.menu_add_assets, R.id.menu_wallet, R.id.menu_transaction, R.id.right_btn, R.id.ll_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_scan:  //scan 按钮
                if (showMenu) {
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                } else {
                    if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                        startActivityForResult(new Intent(getContext(), ActivityScanerCode.class), REQUEST_CODE);
                    }
                }
                break;
            case R.id.ll_receive://receive 按钮
                if (showMenu) {
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                } else {
                    getOperation().forward(ReceiveActivity.class);
                }
                break;
            case R.id.ll_send://send转出 按钮
                if (showMenu) {
                    llMenu.setVisibility(View.GONE);
                    showMenu = false;
                } else {
                    getOperation().forward(SendActivity.class);
                }
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
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    startActivityForResult(new Intent(getContext(), ActivityScanerCode.class), REQUEST_CODE);
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                LyqbLogger.log(result);
            }
        }
    }

    public MainWalletAdapter getmAdapter() {
        return mAdapter;
    }

    public void setMoneyValue(BigDecimal moneyValue) {
        this.moneyValue = moneyValue;
    }

    public TextView getWalletCount() {
        return walletCount;
    }

    public void setListAsset(List<BalanceResult.Asset> listAsset) {
        this.listAsset = listAsset;
    }
}
