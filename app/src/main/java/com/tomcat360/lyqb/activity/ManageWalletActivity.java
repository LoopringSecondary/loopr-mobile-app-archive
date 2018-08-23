package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.ManageWalletListAdapter;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_import)
    Button btnImport;
    @BindView(R.id.btn_generate)
    Button btnGenerate;

    private ManageWalletListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle("管理钱包");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");

        mAdapter = new ManageWalletListAdapter(R.layout.adapter_item_manage_wallet, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    getOperation().forward(WalletSafeActivity.class);
            }
        });
    }

    @OnClick({R.id.btn_import, R.id.btn_generate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_import:
                getOperation().forward(ImportWalletActivity.class);
                break;
            case R.id.btn_generate:
                getOperation().forward(GenerateWalletActivity.class);
                break;
        }
    }
}
