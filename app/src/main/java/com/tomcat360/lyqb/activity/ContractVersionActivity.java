package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContractVersionActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.tv_contract_version)
    TextView tvContractVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contract_version);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_contract_version));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


}
