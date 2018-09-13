package com.tomcat360.lyqb.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.lyqb.walletsdk.listener.BalanceListener;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.lyqb.walletsdk.model.response.data.MarketcapResult;
import com.lyqb.walletsdk.model.response.data.Token;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    TickerView walletCount;

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

    @BindView(R.id.spin_kit)
    SpinKitView progressBar;

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

    private List<BalanceResult.Asset> listAsset = new ArrayList<>(); //  返回的token列表

    private List<Token> tokenList = new ArrayList<>();

    private MarketcapResult marketcapResult;

    private Observable<BalanceResult> balanceObserable;

    private Observable<CombineObservable> refreshObservable;

    private Observable<CombineObservable> createObservable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);
        walletCount.setAnimationInterpolator(new OvershootInterpolator());
        walletCount.setCharacterLists(TickerUtils.provideNumberList());
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refresh();
            refreshLayout.finishRefresh(true);
        });
//        progressBar.setIndeterminateDrawable(new Circle());
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
        address = (String) SPUtils.get(Objects.requireNonNull(getContext()), "address", "");
        walletAddress.setText(address);
    }

    @Override
    protected void initData() {
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
        //        refresh();
    }

    private void initObservable() {
        LyqbLogger.log(address);
        if (this.balanceObserable == null) {
            this.balanceObserable = balanceListener.start()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            this.balanceObserable.subscribe(o1 -> {
                if (!this.tokenList.isEmpty() && this.marketcapResult != null && o1.getTokens() != null && !o1.getTokens()
                        .isEmpty()) {
                    presenter.setTokenLegalPrice(o1.getTokens(), tokenList, marketcapResult);
                }
            }, error -> {
            });
        }
        balanceListener.queryByOwner(address);
        presenter.getMarketcapObservable().subscribe(o1 -> {
            if (!this.tokenList.isEmpty() && !this.listAsset.isEmpty()) {
                Log.d("", "marketcap******************");
                presenter.setTokenLegalPrice(listAsset, tokenList, o1);
            }
        }, error -> {
        });
        if (this.refreshObservable == null) {
            this.refreshObservable = Observable.zip(balanceObserable, presenter.getMarketcapObservable(), (balanceResult, marketcapResult) -> CombineObservable
                    .getInstance(balanceResult.getTokens(), tokenList, marketcapResult));
            this.refreshObservable.subscribe(o1 -> {
                if (!this.tokenList.isEmpty()) {
                    this.marketcapResult = o1.getMarketcapResult();
                    presenter.setTokenLegalPrice(o1.getAssetList(), o1.getTokenList(), o1.getMarketcapResult());
                }
            }, error -> {
            });
        }
        if (this.createObservable == null) {
            this.createObservable = Observable.zip(balanceObserable, presenter.getTokenObservable(), presenter.getMarketcapObservable(), (balanceResult, tokens, marketcap) ->
                    CombineObservable.getInstance(balanceResult.getTokens(), tokens, marketcap));
            this.createObservable.subscribe(o -> {
                this.tokenList = o.getTokenList();
                this.marketcapResult = o.getMarketcapResult();
                presenter.setTokenLegalPrice(o.getAssetList(), o.getTokenList(), o.getMarketcapResult());
                progressBar.setVisibility(View.GONE);
            }, error -> {
            });
        }
    }

    private void refresh() {
        marketcapResult = null;
        listAsset.clear();
        balanceListener.queryByOwner(address);
        presenter.refreshMarketcap();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag) {
            flag = false;
            initObservable();
        } else {
            if (listAsset.isEmpty())
                refresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        this.tokenList.clear();
        this.listAsset.clear();
        this.marketcapResult = null;
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

    public void setWalletCount(String text) {
        if (walletCount != null)
            walletCount.setText(text);
    }

    public void setListAsset(List<BalanceResult.Asset> listAsset) {
        this.listAsset = listAsset;
    }

    public static class CombineObservable {

        private static CombineObservable combineObservable;

        private List<BalanceResult.Asset> assetList;

        private List<Token> tokenList;

        private MarketcapResult marketcapResult;

        public static CombineObservable getInstance(List<BalanceResult.Asset> assetList, List<Token> tokenList, MarketcapResult marketcapResult) {
            if (combineObservable == null) {
                return new CombineObservable(assetList, tokenList, marketcapResult);
            }
            combineObservable.setAssetList(assetList);
            combineObservable.setMarketcapResult(marketcapResult);
            combineObservable.setTokenList(tokenList);
            return combineObservable;
        }

        private CombineObservable() {
        }

        public CombineObservable(List<BalanceResult.Asset> balanceResult, List<Token> tokenList, MarketcapResult marketcapResult) {
            this.assetList = balanceResult;
            this.tokenList = tokenList;
            this.marketcapResult = marketcapResult;
        }

        public List<BalanceResult.Asset> getAssetList() {
            return assetList;
        }

        public void setAssetList(List<BalanceResult.Asset> assetList) {
            this.assetList = assetList;
        }

        public List<Token> getTokenList() {
            return tokenList;
        }

        public void setTokenList(List<Token> tokenList) {
            this.tokenList = tokenList;
        }

        public MarketcapResult getMarketcapResult() {
            return marketcapResult;
        }

        public void setMarketcapResult(MarketcapResult marketcapResult) {
            this.marketcapResult = marketcapResult;
        }
    }
}
