package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.ToastUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportWalletActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
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

    }
}
