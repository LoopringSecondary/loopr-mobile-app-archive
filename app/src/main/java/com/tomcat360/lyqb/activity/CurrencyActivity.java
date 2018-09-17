package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lyqb.walletsdk.model.Currency;
import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.CurrencyUtil;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrencyActivity extends BaseActivity {

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
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_money_type));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
        if (CurrencyUtil.getCurrency(getBaseContext()) == Currency.CNY) {
            ivCnyCheck.setVisibility(View.VISIBLE);
            ivUsdCheck.setVisibility(View.GONE);
        } else {
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
                SPUtils.put(this, "isRecreate", true);
                SPUtils.put(this, "coin", "¥");  //保存货币显示，
                ivCnyCheck.setVisibility(View.VISIBLE);
                ivUsdCheck.setVisibility(View.GONE);
                break;
            case R.id.ll_usd:
                SPUtils.put(this, "isRecreate", true);
                SPUtils.put(this, "coin", "$");
                ivCnyCheck.setVisibility(View.GONE);
                ivUsdCheck.setVisibility(View.VISIBLE);
                break;
        }
    }
}
