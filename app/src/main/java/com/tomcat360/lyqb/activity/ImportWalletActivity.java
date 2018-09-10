package com.tomcat360.lyqb.activity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.adapter.ViewPageAdapter;
import com.tomcat360.lyqb.fragment.ImportKeystoreFragment;
import com.tomcat360.lyqb.fragment.ImportMnemonicFragment;
import com.tomcat360.lyqb.fragment.ImportPrivateKeyFragment;
import com.tomcat360.lyqb.model.eventbusData.KeystoreData;
import com.tomcat360.lyqb.model.eventbusData.MnemonicData;
import com.tomcat360.lyqb.model.eventbusData.PrivateKeyData;
import com.tomcat360.lyqb.utils.AppManager;
import com.tomcat360.lyqb.utils.LyqbLogger;
import com.tomcat360.lyqb.views.TitleView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportWalletActivity extends BaseActivity {

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments;

    private String[] mTitles = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    public void initTitle() {

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        title.setBTitle(getResources().getString(R.string.import_wallet));
        title.clickLeftGoBack(getWContext());
        title.setRightImageButton(R.mipmap.icon_scan, new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {

                startActivityForResult(new Intent(ImportWalletActivity.this, ActivityScanerCode.class), REQUEST_CODE);
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        mTitles[0] = getResources().getString(R.string.mnemonic);
        mTitles[1] = getResources().getString(R.string.keystore);
        mTitles[2] = getResources().getString(R.string.private_key);
        mFragments = new ArrayList<>();
        mFragments.add(new ImportMnemonicFragment());
        mFragments.add(new ImportKeystoreFragment());
        mFragments.add(new ImportPrivateKeyFragment());

        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), mFragments, mTitles));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {

                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result");
                LyqbLogger.log(result);

                if (viewPager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new MnemonicData(result));
                } else if (viewPager.getCurrentItem() == 1) {
                    EventBus.getDefault().post(new KeystoreData(result));

                } else if (viewPager.getCurrentItem() == 2) {
                    EventBus.getDefault().post(new PrivateKeyData(result));

                }
            }
        }
    }

}
