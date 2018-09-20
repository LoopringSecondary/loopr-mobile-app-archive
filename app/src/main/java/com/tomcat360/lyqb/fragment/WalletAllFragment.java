package com.tomcat360.lyqb.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.listener.TransactionListener;
import com.lyqb.walletsdk.model.TxType;
import com.lyqb.walletsdk.model.response.data.Transaction;
import com.lyqb.walletsdk.model.response.data.TransactionPageWrapper;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.activity.DefaultWebViewActivity;
import com.tomcat360.lyqb.adapter.WalletAllAdapter;
import com.tomcat360.lyqb.manager.BalanceDataManager;
import com.tomcat360.lyqb.manager.GasDataManager;
import com.tomcat360.lyqb.manager.MarketcapDataManager;
import com.tomcat360.lyqb.manager.TokenDataManager;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.DateUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.NumberUtils;
import com.tomcat360.lyqb.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 *
 */
public class WalletAllFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private WalletAllAdapter mAdapter;

    private TransactionListener transactionListener = new TransactionListener();

    private GasDataManager gasManager;

    private TokenDataManager tokenManager;

    private MarketcapDataManager priceManager;

    private BalanceDataManager balanceManager;

    private String address;

    private String symbol;

    private List<Transaction> list;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_wallet_all, container, false);
        unbinder = ButterKnife.bind(this, layout);
        if (isAdded()) {
            symbol = getArguments().getString("symbol");
        }
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        gasManager = GasDataManager.getInstance(getContext());
        tokenManager = TokenDataManager.getInstance(getContext());
        priceManager = MarketcapDataManager.getInstance(getContext());
        balanceManager = BalanceDataManager.getInstance(getContext());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        address = (String) SPUtils.get(getContext(), "address", "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);  //助记词提示列表
        mAdapter = new WalletAllAdapter(R.layout.adapter_item_wallet_all, null, symbol);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showDetailDialog(getContext(), list.get(position));
            }
        });
        Observable<TransactionPageWrapper> observable = transactionListener.start();
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TransactionPageWrapper>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(TransactionPageWrapper transactionPageWrapper) {
                LyqbLogger.log(transactionPageWrapper.getData().toString());
                list = transactionPageWrapper.getData();
                mAdapter.setNewData(list);

            }
        });
        transactionListener.queryByOwnerAndSymbol(address, symbol, 1, 20);
    }

    public void showDetailDialog(Context context, Transaction tx) {
        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_trade_detail, null);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setAmount(Transaction tx) {
        String result = "--";
        Double price = priceManager.getPriceBySymbol(tx.getSymbol());
        Double value = tokenManager.getDoubleFromWei(tx.getSymbol(), tx.getValue());
        if (price != null && value != null) {
            int precision = balanceManager.getPrecisionBySymbol(tx.getSymbol());
            String valueShown = NumberUtils.format1(value, precision);
            String currency = CurrencyUtil.format(getContext(), value * price);
            switch (tx.getType()) {
                case SEND:
                case SELL:
                case CONVERT_OUTCOME:
                    valueShown = "-" + valueShown + " " + tx.getSymbol();
                    txAmount.setTextColor(getContext().getResources().getColor(R.color.colorRed));
                    break;
                case BUY:
                case RECEIVE:
                case CONVERT_INCOME:
                    valueShown = "+" + valueShown + " " + tx.getSymbol();
                    txAmount.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                    break;
            }
            result = valueShown + " ≈ " + currency;
        }
        txAmount.setText(result);
    }

    private void setStatus(Transaction tx) {
        switch (tx.getStatus()) {
            case SUCCESS:
                txStatus.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                break;
            case PENDING:
                txStatus.setTextColor(getContext().getResources().getColor(R.color.colorPending));
                break;
            case FAILED:
                txStatus.setTextColor(getContext().getResources().getColor(R.color.colorRed));
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
        String currency = CurrencyUtil.format(getContext(), gasValue * price);
        txGas.setText(gasAmount + " ETH ≈ " + currency);
    }

    private void setDate(Transaction tx) {
        String date = DateUtil.timeStampToDateTime3(tx.getCreateTime());
        txDate.setText(date);
    }
}
