package com.tomcat360.lyqb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletSafeActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.ll_wallet_name)
    LinearLayout llWalletName;
    @BindView(R.id.ll_backup_mnemonic)
    LinearLayout llBackupMnemonic;
    @BindView(R.id.ll_export_private_key)
    LinearLayout llExportPrivateKey;
    @BindView(R.id.ll_export_keystore)
    LinearLayout llExportKeystore;
    @BindView(R.id.ll_clear_records)
    LinearLayout llClearRecords;
    @BindView(R.id.btn_switch)
    Button btnSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_safe);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle((String) SPUtils.get(this, "walletname", "name"));
        title.clickLeftGoBack(getWContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }




    @OnClick({R.id.ll_wallet_name, R.id.ll_backup_mnemonic, R.id.ll_export_private_key, R.id.ll_export_keystore, R.id.ll_clear_records, R.id.btn_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wallet_name:
                getOperation().forward(ReviseWalletNameActivity.class);
                break;
            case R.id.ll_backup_mnemonic:
                getOperation().forward(BackupMnemonicActivity.class);
                break;
            case R.id.ll_export_private_key:
                getOperation().forward(ExportPrivateKeyActivity.class);
                break;
            case R.id.ll_export_keystore:
                getOperation().forward(ExportKeystoreActivity.class);
                break;
            case R.id.ll_clear_records:
                break;
            case R.id.btn_switch:
                break;
        }
    }
}
