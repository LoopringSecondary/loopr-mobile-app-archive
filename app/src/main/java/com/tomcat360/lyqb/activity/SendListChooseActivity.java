package com.tomcat360.lyqb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.TokenChooseAdapter;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.views.TitleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SendListChooseActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private TokenChooseAdapter mAdapter;
    private List<BalanceResult.Token> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_list);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.tokens));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_search, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                getOperation().forward(TokenListSearchActivity.class);
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<BalanceResult.Token> tokens = SPUtils.getDataList(this, "tokens");
        mAdapter = new TokenChooseAdapter(R.layout.adapter_item_token_choose, tokens);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("symbol", list.get(position).getSymbol()); //
//                intent.putExtra("amount", list.get(position).getSymbol()); //
                setResult(1, intent);
                finish();
            }
        });


//        getToken();
    }

    private void getToken() {
        String address = (String) SPUtils.get(this, "address", "");
        Observable<BalanceResult> balance = APP.getLooprSocketService().getBalanceDataStream();
        APP.getLooprSocketService().requestBalance(address);
        balance.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BalanceResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BalanceResult balanceResult) {
                        LyqbLogger.log(balanceResult.toString());
                        list = balanceResult.getTokens();
                        mAdapter.setNewData(balanceResult.getTokens());
                    }
                });
    }
}
