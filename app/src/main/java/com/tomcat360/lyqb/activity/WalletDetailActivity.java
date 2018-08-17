package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.ViewPageAdapter;
import com.tomcat360.lyqb.fragment.ImportKeystoreFragment;
import com.tomcat360.lyqb.fragment.ImportMnemonicFragment;
import com.tomcat360.lyqb.fragment.ImportPrivateKeyFragment;
import com.tomcat360.lyqb.fragment.WalletAllFragment;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.wallet_money)
    TextView walletMoney;
    @BindView(R.id.wallet_dollar)
    TextView walletDollar;
    @BindView(R.id.wallet_qrcode)
    ImageView walletQrcode;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments;
    private String[] mTitles = {"All","Receive","Send","Fail"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    public void initTitle() {
        title.setBTitle("LRC");
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new WalletAllFragment());
        mFragments.add(new WalletAllFragment());
        mFragments.add(new WalletAllFragment());
        mFragments.add(new WalletAllFragment());

        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragments, mTitles));

        tabLayout.setupWithViewPager(viewPager);
    }
}
