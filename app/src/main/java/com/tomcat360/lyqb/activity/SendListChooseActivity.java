package com.tomcat360.lyqb.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyqb.walletsdk.model.response.data.BalanceResult;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.TokenChooseAdapter;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.view.APP;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendListChooseActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TokenChooseAdapter mAdapter;

    private List<BalanceResult.Token> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_list);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
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
        list = APP.getListToken();
        mAdapter = new TokenChooseAdapter(R.layout.adapter_item_token_choose, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SPUtils.put(SendListChooseActivity.this, "send_choose", list.get(position).getSymbol());
                //                SPUtils.put(SendListChooseActivity.this,"send_amount",list.get(position).getBalance().doubleValue());
                Intent intent = new Intent();
                intent.putExtra("symbol", list.get(position).getSymbol()); //
                intent.putExtra("amount", list.get(position).getBalance().doubleValue()); //
                setResult(1, intent);
                finish();
            }
        });
    }
}
