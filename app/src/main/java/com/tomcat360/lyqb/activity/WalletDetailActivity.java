package com.tomcat360.lyqb.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.ViewPageAdapter;
import com.tomcat360.lyqb.fragment.WalletAllFragment;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.btn_receive)
    Button btnReceive;

    @BindView(R.id.btn_send)
    Button btnSend;

    private List<Fragment> mFragments;

    private String[] mTitles = {"All", "Receive", "Send", "Fail"};

    private String symbol;

    private String moneyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        symbol = getIntent().getStringExtra("symbol");
        moneyValue = getIntent().getStringExtra("moneyValue");
        title.setBTitle(symbol);
        walletMoney.setText(moneyValue.length() > 8 ? moneyValue.substring(0, 8) : moneyValue);
        walletDollar.setText(moneyValue.length() > 8 ? moneyValue.substring(0, 8) : moneyValue);
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        WalletAllFragment allFragment = new WalletAllFragment();
        WalletAllFragment allFragment2 = new WalletAllFragment();
        WalletAllFragment allFragment3 = new WalletAllFragment();
        WalletAllFragment allFragment4 = new WalletAllFragment();
        allFragment.setArguments(bundle);
        allFragment2.setArguments(bundle);
        allFragment3.setArguments(bundle);
        allFragment4.setArguments(bundle);
        mFragments.add(allFragment);
        mFragments.add(allFragment2);
        mFragments.add(allFragment3);
        mFragments.add(allFragment4);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragments, mTitles));
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick({R.id.btn_receive, R.id.btn_send, R.id.wallet_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_receive:
                getOperation().forward(ReceiveActivity.class);
                break;
            case R.id.btn_send:
                getOperation().forward(SendActivity.class);
                break;
        }
    }
}
