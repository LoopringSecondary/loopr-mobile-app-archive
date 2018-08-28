package com.tomcat360.lyqb.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.model.response.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.TokenListAdapter;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class TokenListActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TokenListAdapter mAdapter;

    private List<BalanceResult.Token> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_token_list);
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
//        List<BalanceResult.Token> tokens = SPUtils.getDataList(this, "tokens");
        list =  APP.getListToken();
        mAdapter = new TokenListAdapter(R.layout.adapter_item_token_list,list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

    }

}
