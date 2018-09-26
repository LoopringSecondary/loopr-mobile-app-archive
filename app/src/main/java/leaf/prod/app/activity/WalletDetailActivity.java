package leaf.prod.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leaf.prod.walletsdk.model.TxType;
import leaf.prod.walletsdk.model.response.data.Transaction;
import leaf.prod.walletsdk.model.response.data.TransactionPageWrapper;
import leaf.prod.walletsdk.service.LoopringService;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.adapter.WalletAllAdapter;
import leaf.prod.app.manager.BalanceDataManager;
import leaf.prod.app.manager.GasDataManager;
import leaf.prod.app.manager.MarketcapDataManager;
import leaf.prod.app.manager.TokenDataManager;
import leaf.prod.app.utils.CurrencyUtil;
import leaf.prod.app.utils.DateUtil;
import leaf.prod.app.utils.NumberUtils;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.views.TitleView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.wallet_money)
    TickerView walletMoney;

    @BindView(R.id.wallet_dollar)
    TextView walletDollar;

    @BindView(R.id.wallet_qrcode)
    ImageView walletQrcode;

    @BindView(R.id.btn_receive)
    Button btnReceive;

    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private String symbol;

    private WalletAllAdapter mAdapter;

    private LoopringService loopringService = new LoopringService();

    private GasDataManager gasManager;

    private TokenDataManager tokenManager;

    private MarketcapDataManager priceManager;

    private BalanceDataManager balanceManager;

    private String address;

    private List<Transaction> list;

    private int txTotalCount;

    private int currentPageIndex = 1;

    private static final int PAGE_SIZE = 2000;

    /**
     * @param context
     */
    private AlertDialog dialog;

    private TextView txAmount;

    private TextView txStatus;

    private TextView txAddress;

    private TextView txID;

    private TextView txGas;

    private TextView txDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        balanceManager = BalanceDataManager.getInstance(this);
        setContentView(R.layout.activity_wallet_detail);
        ButterKnife.bind(this);
        walletMoney.setAnimationInterpolator(new OvershootInterpolator());
        walletMoney.setCharacterLists(TickerUtils.provideNumberList());
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getTxsFromRelay();
        });
    }

    @Override
    protected void initPresenter() {
        gasManager = GasDataManager.getInstance(this);
        tokenManager = TokenDataManager.getInstance(this);
        priceManager = MarketcapDataManager.getInstance(this);
        balanceManager = BalanceDataManager.getInstance(this);
    }

    @Override
    public void initTitle() {
        symbol = getIntent().getStringExtra("symbol");
        title.setBTitle(symbol);
        title.clickLeftGoBack(getWContext());
        walletMoney.setText(balanceManager.getAssetBySymbol(symbol).getValueShown());
        walletDollar.setText(balanceManager.getAssetBySymbol(symbol).getLegalShown());
        list = new ArrayList<>();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        address = (String) SPUtils.get(this, "address", "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);  //助记词提示列表
        mAdapter = new WalletAllAdapter(R.layout.adapter_item_wallet_all, null, symbol);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> showDetailDialog(mAdapter.getItem(position)));
        mAdapter.setOnLoadMoreListener(() -> {
            if (mAdapter.getData().size() >= txTotalCount) {
                //数据全部加载完毕
                mAdapter.loadMoreEnd();
            } else {
                //成功获取更多数据
                getTxsFromRelay();
                mAdapter.loadMoreComplete();
            }
        }, recyclerView);
        getTxsFromRelay();
    }

    private void getTxsFromRelay() {
        loopringService.getTransactions(address, symbol, currentPageIndex, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TransactionPageWrapper>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshLayout.finishRefresh(false);
                    }

                    @Override
                    public void onNext(TransactionPageWrapper transactionPageWrapper) {
                        list.addAll(transactionPageWrapper.getData());
                        Collections.sort(list, (o1, o2) -> o1.getCreateTime() < o2.getCreateTime() ? 1 : -1);
                        mAdapter.setNewData(list);
                        currentPageIndex += 1;
                        txTotalCount = transactionPageWrapper.getTotal();
                        walletMoney.setText(balanceManager.getAssetBySymbol(symbol).getValueShown());
                        walletDollar.setText(balanceManager.getAssetBySymbol(symbol).getLegalShown());
                        refreshLayout.finishRefresh(true);
                        unsubscribe();
                    }
                });
    }

    public void showDetailDialog(Transaction tx) {
        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.DialogTheme);//
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_trade_detail, null);
            builder.setView(view);
            txAmount = view.findViewById(R.id.tx_detail_amount);
            txStatus = view.findViewById(R.id.tx_detail_status);
            txAddress = view.findViewById(R.id.tx_detail_address);
            txID = view.findViewById(R.id.tx_detail_ID);
            txGas = view.findViewById(R.id.tx_detail_gas);
            txDate = view.findViewById(R.id.receive_date);
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
        }
        setAmount(tx);
        setStatus(tx);
        setAddress(tx);
        setID(tx);
        setGas(tx);
        setDate(tx);
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void setAmount(Transaction tx) {
        String result = "--";
        Double price = priceManager.getPriceBySymbol(tx.getSymbol());
        Double value = tokenManager.getDoubleFromWei(tx.getSymbol(), tx.getValue());
        if (price != null && value != null) {
            int precision = balanceManager.getPrecisionBySymbol(tx.getSymbol());
            String valueShown = NumberUtils.format1(value, precision);
            String currency = CurrencyUtil.format(this, value * price);
            switch (tx.getType()) {
                case SEND:
                case SELL:
                case CONVERT_OUTCOME:
                    valueShown = "-" + valueShown + " " + tx.getSymbol();
                    txAmount.setTextColor(this.getResources().getColor(R.color.colorRed));
                    break;
                case BUY:
                case RECEIVE:
                case CONVERT_INCOME:
                    valueShown = "+" + valueShown + " " + tx.getSymbol();
                    txAmount.setTextColor(this.getResources().getColor(R.color.colorGreen));
                    break;
            }
            result = valueShown + " ≈ " + currency;
        }
        txAmount.setText(result);
    }

    private void setStatus(Transaction tx) {
        switch (tx.getStatus()) {
            case SUCCESS:
                txStatus.setTextColor(this.getResources().getColor(R.color.colorGreen));
                break;
            case PENDING:
                txStatus.setTextColor(this.getResources().getColor(R.color.colorPending));
                break;
            case FAILED:
                txStatus.setTextColor(this.getResources().getColor(R.color.colorRed));
                break;
        }
        txStatus.setText(tx.getStatus().getDescription());
    }

    private void setAddress(Transaction tx) {
        String etherUrl = "https://etherscan.io/address/";
        if (tx.getType() == TxType.RECEIVE) {
            etherUrl += tx.getFrom();
            txAddress.setText(tx.getFrom());
        } else {
            etherUrl += tx.getTo();
            txAddress.setText(tx.getTo());
        }
        final String url = etherUrl;
        txAddress.setOnClickListener(v -> {
            getOperation().addParameter("url", url);
            getOperation().forward(DefaultWebViewActivity.class);
        });
    }

    private void setID(Transaction tx) {
        String etherUrl = "https://etherscan.io/tx/";
        txID.setText(tx.getTxHash());
        etherUrl += tx.getTxHash();
        final String url = etherUrl;
        txID.setOnClickListener(v -> {
            getOperation().addParameter("url", url);
            getOperation().forward(DefaultWebViewActivity.class);
        });
    }

    private void setGas(Transaction tx) {
        String gasAmount = gasManager.getGasAmountInETH(tx.getGas_used(), tx.getGas_price());
        double gasValue = Double.parseDouble(gasAmount);
        Double price = priceManager.getPriceBySymbol("ETH");
        String currency = CurrencyUtil.format(this, gasValue * price);
        txGas.setText(gasAmount + " ETH ≈ " + currency);
    }

    private void setDate(Transaction tx) {
        String date = DateUtil.timeStampToDateTime3(tx.getCreateTime());
        txDate.setText(date);
    }

    @OnClick({R.id.btn_receive, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_receive:
                getOperation().forward(ReceiveActivity.class);
                break;
            case R.id.btn_send:
                getOperation().forward(SendActivity.class);
                break;
        }
    }

    private void setWalletMoney(String text) {
        if (walletMoney != null)
            walletMoney.setText(text);
    }
}
