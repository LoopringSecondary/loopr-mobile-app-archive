package com.tomcat360.lyqb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.listener.TransactionListener;
import com.lyqb.walletsdk.model.response.data.TransactionPageWrapper;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.WalletAllAdapter;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;

import java.util.ArrayList;

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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_wallet_all, container, false);
        unbinder = ButterKnife.bind(this, layout);
        if (isAdded()){
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

        mAdapter = new WalletAllAdapter(R.layout.adapter_item_wallet_all, null,symbol);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        Observable<TransactionPageWrapper> observable = transactionListener.start();
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransactionPageWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TransactionPageWrapper transactionPageWrapper) {
                        LyqbLogger.log(transactionPageWrapper.getData().toString());
                        mAdapter.setNewData(transactionPageWrapper.getData());
                    }
                });
        transactionListener.queryByOwnerAndSymbol(address,symbol,1,20);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
