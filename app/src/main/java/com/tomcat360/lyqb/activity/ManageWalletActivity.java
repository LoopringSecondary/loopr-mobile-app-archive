package com.tomcat360.lyqb.activity;

import android.os.Bundle;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
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

    }
}
