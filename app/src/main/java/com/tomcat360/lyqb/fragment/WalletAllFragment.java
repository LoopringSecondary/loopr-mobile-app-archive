package com.tomcat360.lyqb.fragment;

import java.math.BigDecimal;
import java.util.List;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.listener.TransactionListener;
import com.lyqb.walletsdk.model.response.data.Transaction;
import com.lyqb.walletsdk.model.response.data.TransactionPageWrapper;
import com.lyqb.walletsdk.util.UnitConverter;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.WalletAllAdapter;
import com.tomcat360.lyqb.utils.DateUtil;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;

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

    private String address;

    private String symbol;

    private List<Transaction> list;

    /**
     * @param context
     */
    private AlertDialog dialog;

    private TextView receiveAmount;

    private TextView receiveStatus;

    private TextView receiveTo;

    private TextView receiveForm;

    private TextView receiveDate;

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

    public void showDetailDialog(Context context, Transaction transaction) {

        if (dialog == null) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme);//
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_trade_detail, null);
            builder.setView(view);
            receiveAmount = (TextView) view.findViewById(R.id.receive_amount);
            receiveStatus = (TextView) view.findViewById(R.id.receive_status);
            receiveTo = (TextView) view.findViewById(R.id.receive_to);
            receiveForm = (TextView) view.findViewById(R.id.receive_form);
            receiveDate = (TextView) view.findViewById(R.id.receive_date);

            builder.setCancelable(true);
            dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
        }
        BigDecimal value = UnitConverter.weiToEth(transaction.getValue()); //wei转成eth
        String amount = value.toPlainString().length() > 8 ? value.toPlainString()
                .substring(0, 8) : value.toPlainString();

        receiveAmount.setText(amount + " " + symbol);
        receiveStatus.setText(transaction.getStatus());
        receiveTo.setText(transaction.getTo());
        receiveForm.setText(transaction.getFrom());
        receiveDate.setText(DateUtil.timeStampToDateTime3(transaction.getCreateTime()));

        dialog.show();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        unbinder.unbind();
    }

}
