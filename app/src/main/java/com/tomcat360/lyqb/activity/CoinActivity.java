package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoinActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.ll_cny)
    LinearLayout llCny;
    @BindView(R.id.ll_usd)
    LinearLayout llUsd;
    @BindView(R.id.iv_cny_check)
    ImageView ivCnyCheck;
    @BindView(R.id.iv_usd_check)
    ImageView ivUsdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coin);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle("货币");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        if ((int)SPUtils.get(this,"coin",1) == 1){
            ivCnyCheck.setVisibility(View.VISIBLE);
            ivUsdCheck.setVisibility(View.GONE);
        }else {
            ivCnyCheck.setVisibility(View.GONE);
            ivUsdCheck.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ll_cny, R.id.ll_usd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_cny:
                SPUtils.put(this, "coin", 1);  //保存货币显示，1为人命币，2为美元
                ivCnyCheck.setVisibility(View.VISIBLE);
                ivUsdCheck.setVisibility(View.GONE);
                break;
            case R.id.ll_usd:
                SPUtils.put(this, "coin", 2);
                ivCnyCheck.setVisibility(View.GONE);
                ivUsdCheck.setVisibility(View.VISIBLE);
                break;
        }
    }
}
