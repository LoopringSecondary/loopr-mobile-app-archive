package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.ViewPageAdapter;
import com.tomcat360.lyqb.fragment.ImportKeystoreFragment;
import com.tomcat360.lyqb.fragment.ImportMnemonicFragment;
import com.tomcat360.lyqb.fragment.ImportPrivateKeyFragment;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments;
    private String[] mTitles = {"Mnemonic","Keystore","Private Key"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    public void initTitle() {
        title.setBTitle("Import Wallet");
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_trade_active, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                ToastUtils.toast("11111");
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        mFragments = new ArrayList<>();
        mFragments.add(new ImportMnemonicFragment());
        mFragments.add(new ImportKeystoreFragment());
        mFragments.add(new ImportPrivateKeyFragment());

        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragments, mTitles));

        tabLayout.setupWithViewPager(viewPager);
    }
}
